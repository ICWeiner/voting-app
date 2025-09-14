package com.joker.apostas.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joker.apostas.dto.CreateContestContestantDto;
import com.joker.apostas.dto.CreateContestDto;
import com.joker.apostas.dto.CreateContestantDto;
import com.joker.apostas.model.Contest;
import com.joker.apostas.model.ContestContestant;
import com.joker.apostas.model.Contestant;
import com.joker.apostas.service.ContestContestantService;

@RestController
@RequestMapping("/api/contest-contestants")
public class ContestContestantController {
    private final ContestContestantService contestContestantService;

        public ContestContestantController(ContestContestantService contestContestantService) {
            this.contestContestantService = contestContestantService;
        }

        @PostMapping("/contests")
        public Contest createContest(@RequestBody CreateContestDto dto) {
            return contestContestantService.createContest(dto);
        }

        @PostMapping("/contestants")
        public Contestant createContestant(@RequestBody CreateContestantDto dto) {
            return contestContestantService.createContestant(dto);
        }

        @PostMapping("/contest-contestants")
        public ContestContestant createContestContestant(@RequestBody CreateContestContestantDto dto) {
            return contestContestantService.createContestContestant(dto);
        }

}