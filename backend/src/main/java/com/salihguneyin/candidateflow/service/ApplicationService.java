package com.salihguneyin.candidateflow.service;

import com.salihguneyin.candidateflow.dto.JobApplicationRequest;
import com.salihguneyin.candidateflow.dto.JobApplicationResponse;
import com.salihguneyin.candidateflow.dto.StatusUpdateRequest;
import com.salihguneyin.candidateflow.entity.JobApplication;
import com.salihguneyin.candidateflow.repository.JobApplicationRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final CandidateService candidateService;
    private final JobPostingService jobPostingService;

    public ApplicationService(
            JobApplicationRepository jobApplicationRepository,
            CandidateService candidateService,
            JobPostingService jobPostingService
    ) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.candidateService = candidateService;
        this.jobPostingService = jobPostingService;
    }

    public List<JobApplicationResponse> getAll() {
        return jobApplicationRepository.findAllByOrderByUpdatedAtDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<JobApplicationResponse> getRecent() {
        return jobApplicationRepository.findTop6ByOrderByUpdatedAtDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    public JobApplicationResponse create(JobApplicationRequest request) {
        JobApplication application = new JobApplication();
        application.setCandidate(candidateService.getEntity(request.candidateId()));
        application.setJobPosting(jobPostingService.getEntity(request.jobPostingId()));
        application.setStatus(request.status());
        application.setFitScore(request.fitScore());
        application.setStageNotes(request.stageNotes().trim());

        return toResponse(jobApplicationRepository.save(application));
    }

    public JobApplicationResponse updateStatus(Long id, StatusUpdateRequest request) {
        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new com.salihguneyin.candidateflow.exception.NotFoundException("Application not found"));
        application.setStatus(request.status());
        if (request.stageNotes() != null && !request.stageNotes().isBlank()) {
            application.setStageNotes(request.stageNotes().trim());
        }

        return toResponse(jobApplicationRepository.save(application));
    }

    JobApplicationResponse toResponse(JobApplication application) {
        return new JobApplicationResponse(
                application.getId(),
                application.getCandidate().getId(),
                application.getCandidate().getFullName(),
                application.getCandidate().getPrimaryStack(),
                application.getJobPosting().getId(),
                application.getJobPosting().getTitle(),
                application.getJobPosting().getTeam(),
                application.getStatus(),
                application.getFitScore(),
                application.getStageNotes(),
                application.getAppliedAt(),
                application.getUpdatedAt()
        );
    }
}
