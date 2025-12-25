package com.joker.apostas.repository;

import com.joker.apostas.dto.VoteChoice;
import com.joker.apostas.model.Contest;
import com.joker.apostas.model.Vote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    /** Find a user's vote for a specific contest. Enforces the one-vote-per-contest rule. */
    Optional<Vote> findByUserIdAndContestId(Long userId, Long contestId);

    // Efficiently count votes grouped by choice for a specific contest
    @Query(
            "SELECT v.voteChoice as choice, COUNT(v) as count "
                    + "FROM Vote v WHERE v.contest = :contest GROUP BY v.voteChoice")
    List<VoteCountResult> countVotesByContest(@Param("contest") Contest contest);

    /** Check if a user has already voted for a contest. */
    boolean existsByUserIdAndContestId(Long userId, Long contestId);

    /** Get all votes for a specific contest. */
    List<Vote> findByContestId(Long contestId);

    /** Get all votes by a specific user. */
    List<Vote> findByUserId(Long userId);

    /** Get all votes made today. */
    @Query("SELECT v FROM Vote v WHERE v.date = CURRENT_DATE")
    List<Vote> findAllVotesFromToday();

    // Projection interface to handle the Group By result
    interface VoteCountResult {
        VoteChoice getChoice();

        Long getCount();
    }
}
