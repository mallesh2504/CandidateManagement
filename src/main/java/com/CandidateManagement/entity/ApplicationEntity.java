package com.CandidateManagement.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "job_applications")
public class ApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateEntity candidate;

    @Column(name = "job_id", nullable = false)
    private Long jobId;

    @Enumerated(EnumType.STRING)
    @Column(name = "application_status", length = 50)
    private ApplicationStatus status;

    @Column(name = "application_stage", length = 100)
    private String applicationStage = "APPLICATION_SUBMITTED";

    @Column(name = "application_source", length = 50)
    private String applicationSource;

    @Column(name = "application_date")
    private LocalDateTime applicationDate;

    @Column(name = "application_notes", columnDefinition = "TEXT")
    private String applicationNotes;

    @Column(name = "expected_salary", precision = 12, scale = 2)
    private BigDecimal expectedSalary;

    @Column(name = "application_rating")
    private Integer applicationRating;

    @Column(name = "skill_match_score", precision = 5, scale = 2)
    private BigDecimal skillMatchScore;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_active")
    private Boolean isActive = true;
}