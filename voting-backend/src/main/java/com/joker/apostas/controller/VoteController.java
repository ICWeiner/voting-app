package com.joker.apostas.controller;

import com.joker.apostas.model.Vote;
import com.joker.apostas.repository.VoteRepository;

import org.springframework.security.core.Authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;


import java.util.List;
import java.time.LocalDate;
import java.util.Map;


@RestController
@RequestMapping("/api/vote")
@CrossOrigin(origins = "http://localhost:5173") // or "*"
public class VoteController {
    private final VoteRepository voteRepository;

    private static final Logger log = LoggerFactory.getLogger(VoteController.class);


    public VoteController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public void submitVote(@RequestBody Vote vote, Authentication authentication) {
        log.info("User '{}' is submitting a vote", authentication.getName());
        vote.setTimestamp(LocalDate.now());
        voteRepository.save(vote);
    }

    @GetMapping("/today")
    public List<Vote> getTodayVotes() {
        return voteRepository.findByDate(LocalDate.now());
    }
}
