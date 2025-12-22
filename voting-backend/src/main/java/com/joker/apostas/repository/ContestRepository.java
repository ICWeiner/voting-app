package com.joker.apostas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.joker.apostas.model.Contest;

public interface ContestRepository extends JpaRepository<Contest, Long> {}
