package com.joker.apostas.repository;

import com.joker.apostas.model.Contestant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestantRepository extends JpaRepository<Contestant, Long> {

}
