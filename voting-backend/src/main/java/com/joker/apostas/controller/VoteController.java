package com.joker.apostas.controller;

import com.joker.apostas.model.Vote;
import com.joker.apostas.repository.VoteRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vote")
@CrossOrigin(origins = "http://localhost:5173") // or "*"
public class VoteController {

    @Autowired
    private VoteRepository voteRepository;

    private static final Logger log = LoggerFactory.getLogger(VoteController.class);


    public VoteController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public void submitVote(@RequestBody Vote vote, Authentication authentication) {
        log.info("User '{}' is submitting a vote", authentication.getName());
        voteRepository.save(vote);
    }

    @GetMapping("/today")
    public List<Vote> getTodayVotes() {
        return voteRepository.findAllVotesFromToday();
    }
}
