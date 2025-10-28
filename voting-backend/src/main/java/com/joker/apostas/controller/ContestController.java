package com.joker.apostas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.joker.apostas.model.Contest;
import com.joker.apostas.repository.ContestRepository;

@RestController
public class ContestController {
    private final ContestRepository contestRepository;

        public ContestController(ContestRepository contestRepository) {
            this.contestRepository = contestRepository;
        }

        @GetMapping("/api/contests")
            public List<Contest> getAllContests() {
                return contestRepository.findAll();
        }

}
