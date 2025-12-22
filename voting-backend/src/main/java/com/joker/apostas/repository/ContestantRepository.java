package com.joker.apostas.repository;

import com.joker.apostas.model.Contestant;
import com.joker.apostas.model.enums.StudiesType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContestantRepository extends JpaRepository<Contestant, Long> {

    /** Find a contestant by name (case-insensitive). */
    Optional<Contestant> findByNameIgnoreCase(String name);

    /** Find contestants by partial name match (case-insensitive). */
    List<Contestant> findByNameContainingIgnoreCase(String name);

    /** Find all contestants with a specific studies type. */
    List<Contestant> findByStudies(StudiesType studies);

    /** Find all contestants by profession. */
    List<Contestant> findByProfessionContainingIgnoreCase(String profession);

    /** Find contestants in a specific age range. */
    @Query("SELECT c FROM Contestant c WHERE c.age BETWEEN :minAge AND :maxAge ORDER BY c.age ASC")
    List<Contestant> findByAgeRange(
            @Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);
}
