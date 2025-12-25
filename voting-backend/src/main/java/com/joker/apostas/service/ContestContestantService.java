package com.joker.apostas.service;

import com.joker.apostas.dto.CreateContestContestantDto;
import com.joker.apostas.dto.CreateContestDto;
import com.joker.apostas.dto.CreateContestantDto;
import com.joker.apostas.model.Contest;
import com.joker.apostas.model.ContestContestant;
import com.joker.apostas.model.Contestant;
import com.joker.apostas.model.id.ContestContestantId;

public interface ContestContestantService {
    Contest getContestById(Long id);

    Contestant getContestantById(Long id);

    Contest createContest(CreateContestDto dto);

    Contestant createContestant(CreateContestantDto dto);

    ContestContestant createContestContestant(CreateContestContestantDto dto);

    void deleteContest(Long id);

    void deleteContestant(Long id);

    void deleteContestContestant(ContestContestantId id);

    Contest updateContest(Long id, CreateContestDto dto);

    Contestant updateContestant(Long id, CreateContestantDto dto);

    ContestContestant updateContestContestant(
            ContestContestantId id, CreateContestContestantDto dto);
}
