package com.salihguneyin.candidateflow.dto;

import com.salihguneyin.candidateflow.entity.EmploymentType;
import java.time.LocalDate;

public record JobPostingResponse(
        Long id,
        String title,
        String team,
        String location,
        EmploymentType employmentType,
        String level,
        Integer minSalary,
        Integer maxSalary,
        boolean active,
        LocalDate createdAt
) {
}
