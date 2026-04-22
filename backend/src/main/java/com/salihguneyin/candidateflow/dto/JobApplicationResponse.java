package com.salihguneyin.candidateflow.dto;

import com.salihguneyin.candidateflow.entity.ApplicationStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record JobApplicationResponse(
        Long id,
        Long candidateId,
        String candidateName,
        String candidateStack,
        Long jobPostingId,
        String jobTitle,
        String team,
        ApplicationStatus status,
        Integer fitScore,
        String stageNotes,
        LocalDate appliedAt,
        LocalDateTime updatedAt
) {
}
