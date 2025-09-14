package com.joker.apostas.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.joker.apostas.dto.CreateContestContestantDto;
import com.joker.apostas.dto.CreateContestDto;
import com.joker.apostas.dto.CreateContestantDto;
import com.joker.apostas.model.Contest;
import com.joker.apostas.model.ContestContestant;
import com.joker.apostas.model.Contestant;
import com.joker.apostas.model.id.ContestContestantId;
import com.joker.apostas.repository.ContestContestantRepository;
import com.joker.apostas.repository.ContestRepository;
import com.joker.apostas.repository.ContestantRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ContestContestantService {
    
    private final ContestRepository contestRepository;
    private final ContestantRepository contestantRepository;
    private final ContestContestantRepository contestContestantRepository;

    public ContestContestantService(ContestRepository contestRepository, 
                             ContestantRepository contestantRepository, 
                             ContestContestantRepository contestContestantRepository) {
        this.contestRepository = contestRepository;
        this.contestantRepository = contestantRepository;
        this.contestContestantRepository = contestContestantRepository;
    }

    public Contest createContest(CreateContestDto dto) {
        Contest contest = new Contest();
        contest.setTitle(dto.title);
        contest.setDescription(dto.description);
        contest.setStartDateTime(dto.startDateTime);
        contest.setEndDateTime(dto.endDateTime);
        contest.setPrize(dto.prize);
        return contestRepository.save(contest);
    }

    public Contestant createContestant(CreateContestantDto dto) {
        Contestant contestant = new Contestant();
        contestant.setName(dto.name);
        contestant.setProfession(dto.profession);
        contestant.setAge(dto.age);
        contestant.setStudies(dto.studies);
        contestant.setNotes(dto.notes);
        return contestantRepository.save(contestant);
    }

    public ContestContestant createContestContestant(CreateContestContestantDto dto) {
        Contest contest = this.contestRepository.findById(dto.contestId)
            .orElseThrow(() -> new RuntimeException("Contest not found"));
        Contestant contestant = this.contestantRepository.findById(dto.contestantId)
            .orElseThrow(() -> new RuntimeException("Contestant not found"));

        ContestContestant contestContestant = new ContestContestant();
        contestContestant.setContest(contest);
        contestContestant.setContestant(contestant);
        contestContestant.setIsSuperJoker(dto.isSuperJoker);
        contestContestant.setId(new ContestContestantId(contest.getId(), contestant.getId()));

        contest.getContestContestants().add(contestContestant);
        contestant.getContestContestants().add(contestContestant);

        return this.contestContestantRepository.save(contestContestant);
    }
}