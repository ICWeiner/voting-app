package com.joker.apostas.service;

import com.joker.apostas.dto.ContestStatsDto;
import com.joker.apostas.dto.VoteChoice;
import com.joker.apostas.mapper.ContestMapper;
import com.joker.apostas.model.Contest;
import com.joker.apostas.model.User;
import com.joker.apostas.model.Vote;
import com.joker.apostas.repository.ContestRepository;
import com.joker.apostas.repository.VoteRepository;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

// TODO: write tests for VoteServiceImpl
@RequiredArgsConstructor
@Service
public class VoteServiceImpl implements VoteService {

    private final ContestRepository contestRepository;

    private final VoteRepository voteRepository;

    private final ContestMapper contestMapper;

    public Vote save(Vote vote) {
        return voteRepository.save(vote);
    }

    public Map<VoteChoice, Long> getToday() {
        return voteRepository.findAll().stream()
                .collect(Collectors.groupingBy(Vote::getVoteChoice, Collectors.counting()));
    }

    @Transactional
    public void castVote(User user, VoteChoice choice) {
        // 1. Fetch the Contest
        Contest contest =
                contestRepository
                        .findTodayContest()
                        .orElseThrow(() -> new RuntimeException("No voting scheduled for today."));

        // 2. Validate Time Constraints
        LocalDateTime now = LocalDateTime.now();

        // Ensure contest has started
        if (contest.getStartDateTime() != null && now.isBefore(contest.getStartDateTime())) {
            throw new RuntimeException("Voting has not started yet.");
        }

        // Ensure contest hasn't ended (The "Certain Hour" logic)
        if (contest.getEndDateTime() != null && now.isAfter(contest.getEndDateTime())) {
            throw new RuntimeException("Voting period has ended.");
        }

        // 3. Upsert Logic (Update if exists, Insert if new)
        Vote vote =
                voteRepository
                        .findByUserIdAndContestId(user.getId(), contest.getId())
                        .orElseGet(
                                () -> {
                                    Vote newVote = new Vote();
                                    newVote.setUser(user);
                                    newVote.setContest(contest);
                                    return newVote;
                                });

        // 4. Set/Update the choice
        vote.setVoteChoice(choice);

        // 5. Save
        voteRepository.save(vote);
    }

    public ContestStatsDto getContestStats(LocalDate date) {
        // Find contest for date (or default to today if null)
        LocalDate targetDate = (date != null) ? date : LocalDate.now();

        // Logic to find active contest if date is today, or historical if date is past
        // Simplification: Find by date
        Contest contest =
                contestRepository
                        .findByDate(targetDate)
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "No contest found for date: " + targetDate));

        // Get Stats
        List<VoteRepository.VoteCountResult> counts = voteRepository.countVotesByContest(contest);

        Map<VoteChoice, Long> resultsMap =
                counts.stream()
                        .collect(
                                Collectors.toMap(
                                        VoteRepository.VoteCountResult::getChoice,
                                        VoteRepository.VoteCountResult::getCount));

        // Use the mapper to create the DTO
        return contestMapper.toStatsDto(contest, resultsMap);
    }
}
