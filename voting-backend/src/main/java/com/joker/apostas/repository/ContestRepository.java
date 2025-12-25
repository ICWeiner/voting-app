package com.joker.apostas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.joker.apostas.model.Contest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ContestRepository extends JpaRepository<Contest, Long> {
    // Find the contest active today
    @Query("SELECT c FROM Contest c WHERE CAST(c.startDateTime AS date) = CURRENT_DATE")
    Optional<Contest> findTodayContest();

    // Find a contest that happened on a specific date (assuming 1 per day)
    @Query("SELECT c FROM Contest c WHERE CAST(c.startDateTime AS date) = :date")
    Optional<Contest> findByDate(@Param("date") LocalDate date);
}
