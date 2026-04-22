package com.salihguneyin.candidateflow.repository;

import com.salihguneyin.candidateflow.entity.JobPosting;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    long countByActiveTrue();
    List<JobPosting> findAllByOrderByCreatedAtDesc();
}
