package com.salihguneyin.candidateflow.dto;

import com.salihguneyin.candidateflow.entity.CandidateSource;
import java.time.LocalDate;

public record CandidateResponse(
        Long id,
        String fullName,
        String email,
        String phone,
        String location,
        CandidateSource source,
        Integer yearsOfExperience,
        String primaryStack,
        Integer noticePeriodDays,
        String portfolioUrl,
        LocalDate createdAt
) {
}
