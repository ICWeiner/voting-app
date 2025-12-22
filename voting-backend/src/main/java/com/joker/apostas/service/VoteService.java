package com.joker.apostas.service;

import com.joker.apostas.model.Vote;
import com.joker.apostas.model.enums.VoteChoice;
import com.joker.apostas.repository.VoteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VoteService {

    @Autowired private VoteRepository repo;

    public VoteService(VoteRepository repo) {
        this.repo = repo;
    }

    public Vote save(Vote vote) {
        return repo.save(vote);
    }

    public List<Vote> getAll() {
        return repo.findAll();
    }

    public Map<VoteChoice, Long> getToday() {
        return repo.findAll().stream()
                .collect(Collectors.groupingBy(Vote::getVoteChoice, Collectors.counting()));
    }
}
