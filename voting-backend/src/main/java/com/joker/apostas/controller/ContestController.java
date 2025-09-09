package com.joker.apostas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.joker.apostas.model.Contest;
import com.joker.apostas.service.ContestService;

@RestController
@RequestMapping("/api/contests")
public class ContestController {
    private final ContestService contestService;

    public ContestController(ContestService contestService) {
        this.contestService = contestService;
    }

    @GetMapping
    public List<Contest> getAll() {
        return contestService.getAllContests();
    }

    @PostMapping
    public Contest create(@RequestBody Contest contest) {
        return contestService.createContest(contest);
    }
}
