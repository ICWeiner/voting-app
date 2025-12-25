package com.joker.apostas.service;

import com.joker.apostas.model.Vote;
import com.joker.apostas.model.enums.VoteChoice;
import com.joker.apostas.repository.VoteRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

// TODO review and create interface
@RequiredArgsConstructor
@Service
public class VoteServiceImpl {

    private final VoteRepository repo;

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
