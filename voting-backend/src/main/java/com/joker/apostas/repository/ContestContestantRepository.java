package com.joker.apostas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.joker.apostas.model.ContestContestant;
import com.joker.apostas.model.id.ContestContestantId;

public interface ContestContestantRepository extends JpaRepository<ContestContestant, ContestContestantId> {

}
