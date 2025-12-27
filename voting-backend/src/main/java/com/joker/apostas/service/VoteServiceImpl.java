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
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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
        log.debug("Service: Attempting to cast vote for User ID: {}", user.getId());
        Contest contest =
                contestRepository
                        .findTodayContest()
                        .orElseThrow(() -> new RuntimeException("No voting scheduled for today."));
        log.debug("Found active contest: ID={}, Title='{}'", contest.getId(), contest.getTitle());

        // 2. Validate Time Constraints
        LocalDateTime now = LocalDateTime.now();

        // Ensure contest has started
        if (contest.getStartDateTime() != null && now.isBefore(contest.getStartDateTime())) {
            log.warn(
                    "Vote rejected: Contest '{}' hasn't started yet (Starts: {})",
                    contest.getTitle(),
                    contest.getStartDateTime());
            throw new RuntimeException("Voting has not started yet.");
        }

        // Ensure contest hasn't ended (The "Certain Hour" logic)
        if (contest.getEndDateTime() != null && now.isAfter(contest.getEndDateTime())) {
            log.warn(
                    "Vote rejected: Contest '{}' already ended (Ended: {})",
                    contest.getTitle(),
                    contest.getEndDateTime());
            throw new RuntimeException("Voting period has ended.");
        }

        // 3. Upsert Logic (Update if exists, Insert if new)
        Vote vote =
                voteRepository
                        .findByUserIdAndContestId(user.getId(), contest.getId())
                        .map(
                                existingVote -> {
                                    log.info(
                                            "User '{}' is updating an existing vote for Contest ID {}. Old choice: {}, New choice: {}",
                                            user.getUsername(),
                                            contest.getId(),
                                            existingVote.getVoteChoice(),
                                            choice);
                                    existingVote.setVoteChoice(choice);
                                    return existingVote;
                                })
                        .orElseGet(
                                () -> {
                                    log.info(
                                            "User '{}' is casting a new vote for Contest ID {} with choice: {}",
                                            user.getUsername(),
                                            contest.getId(),
                                            choice);
                                    Vote newVote = new Vote();
                                    newVote.setUser(user);
                                    newVote.setContest(contest);
                                    newVote.setVoteChoice(choice);
                                    return newVote;
                                });
        // 4. Save
        voteRepository.save(vote);
        log.debug("Vote entity saved to database: VoteID={}", vote.getId());
    }

    public ContestStatsDto getContestStats(LocalDate date) {
        log.info("Calculating contest stats for date: {}", date);

        // 1. Find contest for date (or default to today if null)
        LocalDate targetDate = (date != null) ? date : LocalDate.now();

        // 2.  Logic to find active contest if date is today, or historical if date is past
        // Simplification: Find by date
        Contest contest =
                contestRepository
                        .findByDate(targetDate)
                        .orElseThrow(
                                () -> {
                                    log.warn("Stats Request: No contest found for date {}", date);
                                    return new RuntimeException(
                                            "No contest found for date: " + targetDate);
                                });

        // 3. Get Stats
        List<VoteRepository.VoteCountResult> counts = voteRepository.countVotesByContest(contest);
        log.debug(
                "Database returned {} distinct choice counts for contest {}",
                counts.size(),
                contest.getId());

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
