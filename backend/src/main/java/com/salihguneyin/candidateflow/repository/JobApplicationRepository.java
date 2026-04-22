package com.salihguneyin.candidateflow.repository;

import com.salihguneyin.candidateflow.entity.ApplicationStatus;
import com.salihguneyin.candidateflow.entity.JobApplication;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    long countByStatusNotIn(Collection<ApplicationStatus> statuses);
    long countByStatusIn(Collection<ApplicationStatus> statuses);
    List<JobApplication> findTop6ByOrderByUpdatedAtDesc();
    List<JobApplication> findAllByOrderByUpdatedAtDesc();
}
