package com.joker.apostas.repository;

import com.joker.apostas.model.ContestPresenter;
import com.joker.apostas.model.id.ContestPresenterId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContestPresenterRepository
        extends JpaRepository<ContestPresenter, ContestPresenterId> {

    /** Find all presenters for a specific contest. */
    List<ContestPresenter> findByContestId(Long contestId);

    /** Find all contests presented by a specific presenter. */
    List<ContestPresenter> findByPresenterId(Long presenterId);

    /** Check if a presenter is assigned to a contest. */
    boolean existsByContestIdAndPresenterId(Long contestId, Long presenterId);

    /** Find a specific contest-presenter assignment. */
    @Query(
            "SELECT cp FROM ContestPresenter cp WHERE cp.contest.id = :contestId AND cp.presenter.id = :presenterId")
    Optional<ContestPresenter> findByContestAndPresenter(
            @Param("contestId") Long contestId, @Param("presenterId") Long presenterId);

    /** Count presenters for a specific contest. */
    @Query("SELECT COUNT(cp) FROM ContestPresenter cp WHERE cp.contest.id = :contestId")
    long countPresentersForContest(@Param("contestId") Long contestId);
}
