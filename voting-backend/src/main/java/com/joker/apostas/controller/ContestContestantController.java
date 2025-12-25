package com.joker.apostas.controller;

import com.joker.apostas.service.ContestContestantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joker.apostas.dto.CreateContestContestantDto;
import com.joker.apostas.dto.CreateContestDto;
import com.joker.apostas.dto.CreateContestantDto;
import com.joker.apostas.model.Contest;
import com.joker.apostas.model.ContestContestant;
import com.joker.apostas.model.Contestant;
import com.joker.apostas.model.id.ContestContestantId;
import com.joker.apostas.service.ContestContestantServiceImpl;


@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ContestContestantController {
    private final ContestContestantService contestContestantService;

    @GetMapping("/contests/{id}")
    public Contest getContest(@PathVariable Long id) {
        return contestContestantService.getContestById(id);
    }

    @GetMapping("/contestants/{id}")
    public Contestant getContestant(@PathVariable Long id) {
        return contestContestantService.getContestantById(id);
    }

    // Create

    @PostMapping("/contests/create")
    public Contest createContest(@RequestBody CreateContestDto dto) {
        return contestContestantService.createContest(dto);
    }

    @PostMapping("/contestants/create")
    public Contestant createContestant(@RequestBody CreateContestantDto dto) {
        return contestContestantService.createContestant(dto);
    }

    @PostMapping("/contest-contestants/create")
    public ContestContestant createContestContestant(@RequestBody CreateContestContestantDto dto) {
        return contestContestantService.createContestContestant(dto);
    }

    // Delete

    @DeleteMapping("/contests/delete/{id}")
    public void deleteContest(@PathVariable Long id) {
        contestContestantService.deleteContest(id);
    }

    @DeleteMapping("/contestants/delete/{id}")
    public void deleteContestant(@PathVariable Long id) {
        contestContestantService.deleteContestant(id);
    }

    @DeleteMapping("/contest-contestants/delete/{id}")
    public void deleteContestContestant(@PathVariable ContestContestantId id) {
        contestContestantService.deleteContestContestant(id);
    }

    // Update

    @PutMapping("/contests/edit/{id}")
    public Contest updateContest(@PathVariable Long id, @RequestBody CreateContestDto dto) {
        return contestContestantService.updateContest(id, dto);
    }

    @PutMapping("/contestants/edit/{id}")
    public Contestant updateContestant(
            @PathVariable Long id, @RequestBody CreateContestantDto dto) {
        return contestContestantService.updateContestant(id, dto);
    }

    @PutMapping("/contest-contestants/edit/{id}")
    public ContestContestant updateContestContestant(
            @PathVariable ContestContestantId id, @RequestBody CreateContestContestantDto dto) {
        return contestContestantService.updateContestContestant(id, dto);
    }
}
