package com.salihguneyin.candidateflow.dto;

import com.salihguneyin.candidateflow.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StatusUpdateRequest(
        @NotNull ApplicationStatus status,
        @Size(max = 1000) String stageNotes
) {
}
