package com.salihguneyin.candidateflow.dto;

import java.util.List;

public record DashboardResponse(
        List<SummaryCardResponse> summary,
        List<PipelineMetricResponse> pipeline,
        List<JobApplicationResponse> recentApplications
) {
}
