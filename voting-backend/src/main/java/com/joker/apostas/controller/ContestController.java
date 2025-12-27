package com.joker.apostas.controller;

import com.joker.apostas.model.Contest;
import com.joker.apostas.repository.ContestRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class ContestController {
    private final ContestRepository contestRepository;

    @GetMapping("/contests")
    public List<Contest> getAllContests() {
        return contestRepository.findAll();
    }
}
