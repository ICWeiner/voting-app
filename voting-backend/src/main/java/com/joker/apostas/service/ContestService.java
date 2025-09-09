package com.joker.apostas.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joker.apostas.model.Contest;
import com.joker.apostas.repository.ContestRepository;

import java.util.List;

@Service
@Transactional
public class ContestService {
    private final ContestRepository contestRepository;

    public ContestService(ContestRepository contestRepository) {
        this.contestRepository = contestRepository;
    }

    public List<Contest> getAllContests() {
        return contestRepository.findAll();
    }

    public Contest getContestById(Long id) {
        return contestRepository.findById(id).orElseThrow();
    }

    public Contest createContest(Contest contest) {
        return contestRepository.save(contest);
    }
}