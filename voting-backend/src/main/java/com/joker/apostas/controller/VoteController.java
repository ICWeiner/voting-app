package com.joker.apostas.controller;

import com.joker.apostas.dto.ContestStatsDto;
import com.joker.apostas.dto.VoteRequestDto;
import com.joker.apostas.model.User;
import com.joker.apostas.service.VoteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@CrossOrigin(origins = "http://localhost:5173") // or "*"
@RequestMapping("/api/vote")
@RestController
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<VoteRequestDto> submitVote(
            @AuthenticationPrincipal User user, @RequestBody VoteRequestDto voteRequestDto) {
        log.info(
                "REST request to submit vote | User: '{}' | Choice: '{}'",
                user.getUsername(),
                voteRequestDto.getVoteChoice());

        voteService.castVote(user, voteRequestDto.getVoteChoice());

        log.info("Vote successfully processed for user '{}'", user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 2. Get Active/Today's Stats (Shortcut)
    @GetMapping("/active")
    public ResponseEntity<ContestStatsDto> getActiveContestStats() {
        log.info("REST request to get active contest stats");
        ContestStatsDto stats = voteService.getContestStats(LocalDate.now());

        log.debug("Active stats retrieved: {} entries", stats.getResults().size());
        return ResponseEntity.ok(stats);
    }

    // 3. Get History Stats by Date
    @GetMapping("/history")
    public ResponseEntity<ContestStatsDto> getHistoricalStats(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("REST request to get historical stats for date: {}", date);
        return ResponseEntity.ok(voteService.getContestStats(date));
    }
}
