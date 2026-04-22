package com.salihguneyin.candidateflow.config;

import com.salihguneyin.candidateflow.entity.ApplicationStatus;
import com.salihguneyin.candidateflow.entity.Candidate;
import com.salihguneyin.candidateflow.entity.CandidateSource;
import com.salihguneyin.candidateflow.entity.EmploymentType;
import com.salihguneyin.candidateflow.entity.JobApplication;
import com.salihguneyin.candidateflow.entity.JobPosting;
import com.salihguneyin.candidateflow.repository.CandidateRepository;
import com.salihguneyin.candidateflow.repository.JobApplicationRepository;
import com.salihguneyin.candidateflow.repository.JobPostingRepository;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(
            CandidateRepository candidateRepository,
            JobPostingRepository jobPostingRepository,
            JobApplicationRepository jobApplicationRepository
    ) {
        return args -> {
            if (candidateRepository.count() > 0) {
                return;
            }

            Candidate salih = candidate("Salih Guneyin", "salih@example.com", "0553 168 33 41", "Gebze", CandidateSource.GITHUB, 2, "Java, Spring Boot, MSSQL", 15, "https://github.com/salih");
            Candidate elif = candidate("Elif Yilmaz", "elif@example.com", "0532 111 22 33", "Istanbul", CandidateSource.LINKEDIN, 3, "React, TypeScript, Jest", 30, "https://github.com/elif");
            Candidate mert = candidate("Mert Kaya", "mert@example.com", "0535 444 55 66", "Ankara", CandidateSource.REFERRAL, 4, "Node.js, PostgreSQL, Docker", 21, null);

            candidateRepository.save(salih);
            candidateRepository.save(elif);
            candidateRepository.save(mert);

            JobPosting backendIntern = job("Backend Developer Intern", "Platform", "Hybrid / Istanbul", EmploymentType.INTERN, "Junior", 22000, 28000, true);
            JobPosting frontendEngineer = job("Frontend Engineer", "Growth", "Remote / Turkey", EmploymentType.FULL_TIME, "Mid", 50000, 70000, true);
            JobPosting qaAnalyst = job("QA Automation Analyst", "Quality", "Hybrid / Kocaeli", EmploymentType.FULL_TIME, "Junior", 35000, 50000, false);

            jobPostingRepository.save(backendIntern);
            jobPostingRepository.save(frontendEngineer);
            jobPostingRepository.save(qaAnalyst);

            jobApplicationRepository.save(application(salih, backendIntern, ApplicationStatus.INTERVIEW, 88, "Strong Spring Boot fundamentals and clean GitHub structure.", LocalDate.now().minusDays(3)));
            jobApplicationRepository.save(application(elif, frontendEngineer, ApplicationStatus.SCREENING, 82, "Good test coverage mindset and component design.", LocalDate.now().minusDays(5)));
            jobApplicationRepository.save(application(mert, qaAnalyst, ApplicationStatus.REJECTED, 61, "Automation basics are solid but API test experience is limited.", LocalDate.now().minusDays(10)));
        };
    }

    private Candidate candidate(
            String fullName,
            String email,
            String phone,
            String location,
            CandidateSource source,
            int yearsOfExperience,
            String primaryStack,
            int noticePeriodDays,
            String portfolioUrl
    ) {
        Candidate candidate = new Candidate();
        candidate.setFullName(fullName);
        candidate.setEmail(email);
        candidate.setPhone(phone);
        candidate.setLocation(location);
        candidate.setSource(source);
        candidate.setYearsOfExperience(yearsOfExperience);
        candidate.setPrimaryStack(primaryStack);
        candidate.setNoticePeriodDays(noticePeriodDays);
        candidate.setPortfolioUrl(portfolioUrl);
        return candidate;
    }

    private JobPosting job(
            String title,
            String team,
            String location,
            EmploymentType employmentType,
            String level,
            int minSalary,
            int maxSalary,
            boolean active
    ) {
        JobPosting jobPosting = new JobPosting();
        jobPosting.setTitle(title);
        jobPosting.setTeam(team);
        jobPosting.setLocation(location);
        jobPosting.setEmploymentType(employmentType);
        jobPosting.setLevel(level);
        jobPosting.setMinSalary(minSalary);
        jobPosting.setMaxSalary(maxSalary);
        jobPosting.setActive(active);
        return jobPosting;
    }

    private JobApplication application(
            Candidate candidate,
            JobPosting jobPosting,
            ApplicationStatus status,
            int fitScore,
            String stageNotes,
            LocalDate appliedAt
    ) {
        JobApplication application = new JobApplication();
        application.setCandidate(candidate);
        application.setJobPosting(jobPosting);
        application.setStatus(status);
        application.setFitScore(fitScore);
        application.setStageNotes(stageNotes);
        application.setAppliedAt(appliedAt);
        return application;
    }
}
