package com.joker.apostas.repository;

import com.joker.apostas.model.ContestContestant;
import com.joker.apostas.model.id.ContestContestantId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestContestantRepository
        extends JpaRepository<ContestContestant, ContestContestantId> {

    /** Find all contestants for a specific contest. */
    List<ContestContestant> findByContestId(Long contestId);

    /** Find all contests for a specific contestant. */
    List<ContestContestant> findByContestantId(Long contestantId);

    /** Check if a contestant participates in a contest. */
    boolean existsByContestIdAndContestantId(Long contestId, Long contestantId);
}
