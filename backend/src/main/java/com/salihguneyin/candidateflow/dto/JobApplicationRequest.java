package com.salihguneyin.candidateflow.dto;

import com.salihguneyin.candidateflow.entity.ApplicationStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record JobApplicationRequest(
        @NotNull Long candidateId,
        @NotNull Long jobPostingId,
        @NotNull ApplicationStatus status,
        @NotNull @Min(1) @Max(100) Integer fitScore,
        @NotBlank @Size(max = 1000) String stageNotes
) {
}
