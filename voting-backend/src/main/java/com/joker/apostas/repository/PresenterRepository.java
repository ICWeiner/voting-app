package com.joker.apostas.repository;

import com.joker.apostas.model.Presenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PresenterRepository extends JpaRepository<Presenter, Long> {

    /** Find a presenter by name (case-insensitive). */
    Optional<Presenter> findByNameIgnoreCase(String name);

    /** Find presenters by partial name match (case-insensitive). */
    List<Presenter> findByNameContainingIgnoreCase(String name);

    /** Check if a presenter name already exists. */
    boolean existsByNameIgnoreCase(String name);

    /** Count how many contests a presenter is involved in. */
    @Query("SELECT COUNT(cp) FROM ContestPresenter cp WHERE cp.presenter.id = :presenterId")
    long countContestAssignments(@Param("presenterId") Long presenterId);
}
