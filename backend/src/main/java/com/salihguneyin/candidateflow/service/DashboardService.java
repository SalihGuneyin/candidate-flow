package com.salihguneyin.candidateflow.service;

import com.salihguneyin.candidateflow.dto.DashboardResponse;
import com.salihguneyin.candidateflow.dto.PipelineMetricResponse;
import com.salihguneyin.candidateflow.dto.SummaryCardResponse;
import com.salihguneyin.candidateflow.entity.ApplicationStatus;
import com.salihguneyin.candidateflow.repository.CandidateRepository;
import com.salihguneyin.candidateflow.repository.JobApplicationRepository;
import com.salihguneyin.candidateflow.repository.JobPostingRepository;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final CandidateRepository candidateRepository;
    private final JobPostingRepository jobPostingRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final ApplicationService applicationService;

    public DashboardService(
            CandidateRepository candidateRepository,
            JobPostingRepository jobPostingRepository,
            JobApplicationRepository jobApplicationRepository,
            ApplicationService applicationService
    ) {
        this.candidateRepository = candidateRepository;
        this.jobPostingRepository = jobPostingRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.applicationService = applicationService;
    }

    public DashboardResponse getDashboard() {
        List<SummaryCardResponse> summary = List.of(
                new SummaryCardResponse("Candidates", candidateRepository.count(), "ink"),
                new SummaryCardResponse("Open Roles", jobPostingRepository.countByActiveTrue(), "mint"),
                new SummaryCardResponse(
                        "Live Pipeline",
                        jobApplicationRepository.countByStatusNotIn(List.of(ApplicationStatus.HIRED, ApplicationStatus.REJECTED)),
                        "gold"
                ),
                new SummaryCardResponse(
                        "Interview Ready",
                        jobApplicationRepository.countByStatusIn(List.of(ApplicationStatus.SCREENING, ApplicationStatus.INTERVIEW)),
                        "rose"
                )
        );

        List<PipelineMetricResponse> pipeline = Arrays.stream(ApplicationStatus.values())
                .map(status -> new PipelineMetricResponse(
                        status.name(),
                        jobApplicationRepository.findAll().stream().filter(application -> application.getStatus() == status).count()
                ))
                .toList();

        return new DashboardResponse(summary, pipeline, applicationService.getRecent());
    }
}
