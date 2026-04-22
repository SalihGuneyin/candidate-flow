package com.salihguneyin.candidateflow.service;

import com.salihguneyin.candidateflow.dto.JobPostingRequest;
import com.salihguneyin.candidateflow.dto.JobPostingResponse;
import com.salihguneyin.candidateflow.entity.JobPosting;
import com.salihguneyin.candidateflow.exception.NotFoundException;
import com.salihguneyin.candidateflow.repository.JobPostingRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class JobPostingService {

    private final JobPostingRepository jobPostingRepository;

    public JobPostingService(JobPostingRepository jobPostingRepository) {
        this.jobPostingRepository = jobPostingRepository;
    }

    public List<JobPostingResponse> getAll() {
        return jobPostingRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    public JobPosting getEntity(Long id) {
        return jobPostingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Job posting not found"));
    }

    public JobPostingResponse create(JobPostingRequest request) {
        if (request.minSalary() > request.maxSalary()) {
            throw new IllegalArgumentException("Minimum salary cannot be greater than maximum salary");
        }

        JobPosting jobPosting = new JobPosting();
        jobPosting.setTitle(request.title().trim());
        jobPosting.setTeam(request.team().trim());
        jobPosting.setLocation(request.location().trim());
        jobPosting.setEmploymentType(request.employmentType());
        jobPosting.setLevel(request.level().trim());
        jobPosting.setMinSalary(request.minSalary());
        jobPosting.setMaxSalary(request.maxSalary());
        jobPosting.setActive(request.active());

        return toResponse(jobPostingRepository.save(jobPosting));
    }

    JobPostingResponse toResponse(JobPosting jobPosting) {
        return new JobPostingResponse(
                jobPosting.getId(),
                jobPosting.getTitle(),
                jobPosting.getTeam(),
                jobPosting.getLocation(),
                jobPosting.getEmploymentType(),
                jobPosting.getLevel(),
                jobPosting.getMinSalary(),
                jobPosting.getMaxSalary(),
                jobPosting.isActive(),
                jobPosting.getCreatedAt()
        );
    }
}
