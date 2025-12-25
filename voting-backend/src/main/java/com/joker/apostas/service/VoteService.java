package com.joker.apostas.service;

import com.joker.apostas.dto.ContestStatsDto;
import com.joker.apostas.dto.VoteChoice;
import com.joker.apostas.model.User;

import jakarta.transaction.Transactional;

import java.time.LocalDate;

public interface VoteService {
    @Transactional
    void castVote(User user, VoteChoice choice);

    ContestStatsDto getContestStats(LocalDate date);
}
