package com.joker.apostas.service;

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

    public Contest getContestById(Long id) {
        return contestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contest not found with id " + id));
    }

    public Contestant getContestantById(Long id) {
        return contestantRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contestant not found with id " + id));
    }

    //Create

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

        ContestContestant contestContestant = new ContestContestant(contest, contestant, dto.isSuperJoker);

        return this.contestContestantRepository.save(contestContestant);
    }


    //Delete

    @Transactional
    public void deleteContest(Long id) {
        Contest contest = contestRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contest not found with id " + id));

        contest.getContestContestants().forEach(cc -> {
            Contestant contestant = cc.getContestant();
            contestContestantRepository.delete(cc);
            contestantRepository.delete(contestant);
        });

        contestRepository.delete(contest);
    }


    public void deleteContestant(Long id) {
        if (!contestantRepository.existsById(id)) {
            throw new RuntimeException("Contestant not found with id " + id);
        }
        contestantRepository.deleteById(id);
    }

    public void deleteContestContestant(ContestContestantId id) {
        if (!contestContestantRepository.existsById(id)) {
            throw new RuntimeException("ContestContestant not found with id " + id);
        }
        contestContestantRepository.deleteById(id);
    }


    //Update

    public Contest updateContest(Long id, CreateContestDto dto) {         
        Contest contest = contestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contest not found with id " + id));

        contest.setTitle(dto.title);
        contest.setDescription(dto.description);
        contest.setStartDateTime(dto.startDateTime);
        contest.setEndDateTime(dto.endDateTime);
        contest.setPrize(dto.prize);

        return contestRepository.save(contest);
    }

    public Contestant updateContestant(Long id, CreateContestantDto dto) {
        Contestant contestant = contestantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contestant not found with id " + id));

        contestant.setName(dto.name);
        contestant.setProfession(dto.profession);
        contestant.setAge(dto.age);
        contestant.setStudies(dto.studies);
        contestant.setNotes(dto.notes);

        return contestantRepository.save(contestant);
    }

    public ContestContestant updateContestContestant(ContestContestantId id, CreateContestContestantDto dto) {
        ContestContestant cc = contestContestantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ContestContestant not found with id " + id));

        Contest contest = contestRepository.findById(dto.contestId)
                .orElseThrow(() -> new RuntimeException("Contest not found with id " + dto.contestId));
        cc.setContest(contest);

        Contestant contestant = contestantRepository.findById(dto.contestantId)
                .orElseThrow(() -> new RuntimeException("Contestant not found with id " + dto.contestantId));
        cc.setContestant(contestant);

        cc.setIsSuperJoker(dto.isSuperJoker != null ? dto.isSuperJoker : cc.getIsSuperJoker());

        return contestContestantRepository.save(cc);
    }

}