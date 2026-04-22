package com.salihguneyin.candidateflow.dto;

import com.salihguneyin.candidateflow.entity.EmploymentType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record JobPostingRequest(
        @NotBlank String title,
        @NotBlank String team,
        @NotBlank String location,
        @NotNull EmploymentType employmentType,
        @NotBlank String level,
        @NotNull @Min(10000) @Max(500000) Integer minSalary,
        @NotNull @Min(10000) @Max(500000) Integer maxSalary,
        boolean active
) {
}
