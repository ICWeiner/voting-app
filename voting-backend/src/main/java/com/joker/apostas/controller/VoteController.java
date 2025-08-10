package com.joker.apostas.controller;

import com.joker.apostas.model.Vote;
import com.joker.apostas.repository.VoteRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/vote")
@CrossOrigin(origins = "http://localhost:5173") // or "*"
public class VoteController {
    private final VoteRepository voteRepository;

    public VoteController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @PostMapping
    public void submitVote(@RequestBody Vote vote) {
        vote.setTimestamp(LocalDate.now());
        voteRepository.save(vote);
    }

    @GetMapping("/today")
    public List<Vote> getTodayVotes() {
        return voteRepository.findByDate(LocalDate.now());
    }
}
