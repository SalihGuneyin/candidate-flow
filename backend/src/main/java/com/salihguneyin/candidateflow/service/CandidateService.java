package com.salihguneyin.candidateflow.service;

import com.salihguneyin.candidateflow.dto.CandidateRequest;
import com.salihguneyin.candidateflow.dto.CandidateResponse;
import com.salihguneyin.candidateflow.entity.Candidate;
import com.salihguneyin.candidateflow.repository.CandidateRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;

    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public List<CandidateResponse> getAll() {
        return candidateRepository.findAll().stream()
                .sorted((left, right) -> right.getCreatedAt().compareTo(left.getCreatedAt()))
                .map(this::toResponse)
                .toList();
    }

    public Candidate getEntity(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new com.salihguneyin.candidateflow.exception.NotFoundException("Candidate not found"));
    }

    public CandidateResponse create(CandidateRequest request) {
        if (candidateRepository.existsByEmailIgnoreCase(request.email())) {
            throw new IllegalArgumentException("A candidate with this email already exists");
        }

        Candidate candidate = new Candidate();
        candidate.setFullName(request.fullName().trim());
        candidate.setEmail(request.email().trim().toLowerCase());
        candidate.setPhone(request.phone().trim());
        candidate.setLocation(request.location().trim());
        candidate.setSource(request.source());
        candidate.setYearsOfExperience(request.yearsOfExperience());
        candidate.setPrimaryStack(request.primaryStack().trim());
        candidate.setNoticePeriodDays(request.noticePeriodDays());
        candidate.setPortfolioUrl(request.portfolioUrl() == null ? null : request.portfolioUrl().trim());

        return toResponse(candidateRepository.save(candidate));
    }

    CandidateResponse toResponse(Candidate candidate) {
        return new CandidateResponse(
                candidate.getId(),
                candidate.getFullName(),
                candidate.getEmail(),
                candidate.getPhone(),
                candidate.getLocation(),
                candidate.getSource(),
                candidate.getYearsOfExperience(),
                candidate.getPrimaryStack(),
                candidate.getNoticePeriodDays(),
                candidate.getPortfolioUrl(),
                candidate.getCreatedAt()
        );
    }
}
