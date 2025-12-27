package com.joker.apostas.service;

import com.joker.apostas.dto.VoteChoice;
import com.joker.apostas.mapper.ContestMapper;
import com.joker.apostas.model.Contest;
import com.joker.apostas.model.User;
import com.joker.apostas.model.Vote;
import com.joker.apostas.repository.ContestRepository;
import com.joker.apostas.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoteServiceImplTest {

    @Mock private ContestRepository contestRepository;
    @Mock private VoteRepository voteRepository;
    @Mock
    private ContestMapper contestMapper;

    @InjectMocks
    private VoteServiceImpl voteService;

    private User testUser;
    private Contest activeContest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);

        activeContest = new Contest();
        activeContest.setId(10L);
        activeContest.setStartDateTime(LocalDateTime.now().minusHours(1));
        activeContest.setEndDateTime(LocalDateTime.now().plusHours(1));
    }

    @Test
    void castVote_NewVote_ShouldSaveSuccessfully() {
        // Arrange
        when(contestRepository.findTodayContest()).thenReturn(Optional.of(activeContest));
        when(voteRepository.findByUserIdAndContestId(1L, 10L)).thenReturn(Optional.empty());

        // Act
        voteService.castVote(testUser, VoteChoice.V200);

        // Assert
        ArgumentCaptor<Vote> voteCaptor = ArgumentCaptor.forClass(Vote.class);
        verify(voteRepository).save(voteCaptor.capture());
        assertEquals(VoteChoice.V200, voteCaptor.getValue().getVoteChoice());
        assertEquals(testUser, voteCaptor.getValue().getUser());
    }

    @Test
    void castVote_OutsideTimeWindow_ShouldThrowException() {
        // Arrange: Contest ended 10 mins ago
        activeContest.setEndDateTime(LocalDateTime.now().minusMinutes(10));
        when(contestRepository.findTodayContest()).thenReturn(Optional.of(activeContest));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                voteService.castVote(testUser, VoteChoice.V500)
        );
    }
}