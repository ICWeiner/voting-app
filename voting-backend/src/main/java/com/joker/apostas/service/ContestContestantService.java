package com.joker.apostas.service;

import org.springframework.stereotype.Service;

import com.joker.apostas.model.ContestContestant;
import com.joker.apostas.repository.ContestContestantRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ContestContestantService {
    private final ContestContestantRepository repository;

    public ContestContestantService(ContestContestantRepository repository) {
        this.repository = repository;
    }

    public ContestContestant assignContestantToContest(ContestContestant cc) {
        return repository.save(cc);
    }
}