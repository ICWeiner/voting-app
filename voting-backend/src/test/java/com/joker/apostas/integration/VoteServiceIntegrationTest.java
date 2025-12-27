package com.joker.apostas.integration;

import com.joker.apostas.dto.ContestStatus;
import com.joker.apostas.dto.VoteChoice;
import com.joker.apostas.model.Contest;
import com.joker.apostas.model.User;
import com.joker.apostas.model.Vote;
import com.joker.apostas.model.enums.Role;
import com.joker.apostas.repository.ContestRepository;
import com.joker.apostas.repository.UserRepository;
import com.joker.apostas.repository.VoteRepository;
import com.joker.apostas.service.VoteService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
public class VoteServiceIntegrationTest extends AbstractIntegrationTest{

    @Autowired
    private VoteService voteService;
    @Autowired private ContestRepository contestRepository;
    @Autowired private VoteRepository voteRepository;
    @Autowired private UserRepository userRepository;

    @Test
    void testCastVotePersistence() {
        // 1. Prepare Data TODO i dont love user creation here
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("pass");
        user.setRole(Role.USER);
        userRepository.save(user);

        Contest contest = new Contest();
        contest.setTitle("Daily Contest");
        contest.setStartDateTime(LocalDateTime.now().minusHours(1));
        contest.setEndDateTime(LocalDateTime.now().plusHours(5));
        contest.setStatus(ContestStatus.OPEN);
        // Important: findTodayContest usually looks for a specific date/status
        contestRepository.save(contest);

        // 2. Act
        voteService.castVote(user, VoteChoice.V1000);

        // 3. Assert
        Optional<Vote> savedVote = voteRepository.findByUserIdAndContestId(user.getId(), contest.getId());
        assertTrue(savedVote.isPresent());
        assertEquals(VoteChoice.V1000, savedVote.get().getVoteChoice());
    }
}
