package com.salihguneyin.candidateflow.dto;

import com.salihguneyin.candidateflow.entity.CandidateSource;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CandidateRequest(
        @NotBlank String fullName,
        @NotBlank @Email String email,
        @NotBlank String phone,
        @NotBlank String location,
        @NotNull CandidateSource source,
        @NotNull @Min(0) @Max(25) Integer yearsOfExperience,
        @NotBlank String primaryStack,
        @NotNull @Min(0) @Max(365) Integer noticePeriodDays,
        @Size(max = 255) String portfolioUrl
) {
}
