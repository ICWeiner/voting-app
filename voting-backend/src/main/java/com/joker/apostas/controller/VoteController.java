package com.joker.apostas.controller;

import com.joker.apostas.dto.ContestStatsDto;
import com.joker.apostas.dto.VoteRequestDto;
import com.joker.apostas.model.User;
import com.joker.apostas.service.VoteService;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RequestMapping("/vote")
@CrossOrigin(origins = "http://localhost:5173") // or "*"
@RestController
public class VoteController {

    private final VoteService voteService;

    private static final Logger log = LoggerFactory.getLogger(VoteController.class);

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<VoteRequestDto> submitVote(
            @AuthenticationPrincipal User user, @RequestBody VoteRequestDto voteRequestDto) {
        log.info("User '{}' is submitting a vote", user.getUsername());
        voteService.castVote(user, voteRequestDto.getVoteChoice());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 2. Get Active/Today's Stats (Shortcut)
    @GetMapping("/active")
    public ResponseEntity<ContestStatsDto> getActiveContestStats() {
        return ResponseEntity.ok(voteService.getContestStats(LocalDate.now()));
    }

    // 3. Get History Stats by Date
    @GetMapping("/history")
    public ResponseEntity<ContestStatsDto> getHistoricalStats(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(voteService.getContestStats(date));
    }
}
