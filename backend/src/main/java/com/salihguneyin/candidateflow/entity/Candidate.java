package com.salihguneyin.candidateflow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CandidateSource source;

    @Column(nullable = false)
    private Integer yearsOfExperience;

    @Column(nullable = false)
    private String primaryStack;

    @Column(nullable = false)
    private Integer noticePeriodDays;

    private String portfolioUrl;

    @Column(nullable = false)
    private LocalDate createdAt;

    @PrePersist
    void prePersist() {
        createdAt = createdAt == null ? LocalDate.now() : createdAt;
    }
}
