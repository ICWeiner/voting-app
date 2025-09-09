package com.joker.apostas.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joker.apostas.model.ContestContestant;
import com.joker.apostas.service.ContestContestantService;

@RestController
@RequestMapping("/api/contest-contestants")
public class ContestContestantController {
    private final ContestContestantService service;

    public ContestContestantController(ContestContestantService service) {
        this.service = service;
    }

    @PostMapping
    public ContestContestant assign(@RequestBody ContestContestant cc) {
        return service.assignContestantToContest(cc);
    }
}