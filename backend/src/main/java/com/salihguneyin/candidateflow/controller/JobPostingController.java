package com.salihguneyin.candidateflow.controller;

import com.salihguneyin.candidateflow.dto.JobPostingRequest;
import com.salihguneyin.candidateflow.dto.JobPostingResponse;
import com.salihguneyin.candidateflow.service.JobPostingService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/job-postings")
public class JobPostingController {

    private final JobPostingService jobPostingService;

    public JobPostingController(JobPostingService jobPostingService) {
        this.jobPostingService = jobPostingService;
    }

    @GetMapping
    public List<JobPostingResponse> getAll() {
        return jobPostingService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobPostingResponse create(@Valid @RequestBody JobPostingRequest request) {
        return jobPostingService.create(request);
    }
}
