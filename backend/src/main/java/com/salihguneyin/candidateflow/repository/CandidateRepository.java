package com.salihguneyin.candidateflow.repository;

import com.salihguneyin.candidateflow.entity.Candidate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    boolean existsByEmailIgnoreCase(String email);
    Optional<Candidate> findByEmailIgnoreCase(String email);
}
