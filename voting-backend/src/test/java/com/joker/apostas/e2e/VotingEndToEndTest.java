package com.joker.apostas.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joker.apostas.dto.ContestStatus;
import com.joker.apostas.dto.VoteChoice;
import com.joker.apostas.model.Contest;
import com.joker.apostas.repository.ContestRepository;

import com.joker.apostas.repository.VoteRepository;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class VotingEndToEndTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ContestRepository contestRepository;
    @Autowired private VoteRepository voteRepository;

    @BeforeEach
    void setupContest() {
        // We need an active contest in the DB for the E2E flow to work
        Contest contest = new Contest();
        contest.setTitle("E2E Championship");
        contest.setStatus(ContestStatus.OPEN);
        contest.setStartDateTime(LocalDateTime.now().minusHours(1));
        contest.setEndDateTime(LocalDateTime.now().plusHours(1));
        contestRepository.save(contest);
    }

    @Test
    void fullUserVotingJourney() throws Exception {
        // --- 1. REGISTER ---
        String signupJson = """
            {
                "username": "joker_e2e",
                "password": "securePassword123",
                "email": "joker@example.com"
            }
            """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupJson))
                .andExpect(status().isCreated());

        // --- 2. LOGIN & GET JWT ---
        String loginJson = "{\"identifier\": \"joker_e2e\", \"password\": \"securePassword123\"}";

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Extract token (Adjust path based on your AuthResponse DTO)
        String token = objectMapper.readTree(loginResponse).get("token").asText();
        String authHeader = "Bearer " + token;

        // --- 3. FIRST VOTE (Choice: 500) ---
        String firstVoteJson = "{\"VoteChoice\": \"V500\"}";

        mockMvc.perform(post("/api/vote")
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(firstVoteJson))
                .andExpect(status().isCreated());

        // Verify DB state
        assertEquals(1, voteRepository.findAll().size());
        assertEquals(VoteChoice.V500, voteRepository.findAll().getFirst().getVoteChoice());

        // --- 4. RE-VOTE (Choice: 1000) ---
        String secondVoteJson = "{\"VoteChoice\": \"V1000\"}";

        mockMvc.perform(post("/api/vote")
                        .header("Authorization", authHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(secondVoteJson))
                .andExpect(status().isCreated());

        // --- 5. FINAL VALIDATION ---
        // Ensure only 1 record exists (Upsert worked) and choice is updated
        assertEquals(1, voteRepository.findAll().size());
        assertEquals(VoteChoice.V1000, voteRepository.findAll().getFirst().getVoteChoice());
    }
}