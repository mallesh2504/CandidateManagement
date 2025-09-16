package com.CandidateManagement.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "candidate_profiles")
public class CandidateProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateEntity candidate;

    @Column(name = "linkedin_url", length = 200)
    private String linkedinUrl;

    @Column(name = "github_url", length = 200)
    private String githubUrl;

    @Column(name = "portfolio_url", length = 200)
    private String portfolioUrl;

    @Column(name = "website_url", length = 200)
    private String websiteUrl;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    @Column(name = "gender", length = 20)
    private String gender;

    @Column(name = "nationality", length = 50)
    private String nationality;

    @Column(name = "availability_status", length = 50)
    private String availabilityStatus;

    @Column(name = "availability_date")
    private Timestamp availabilityDate;

    @Column(name = "salary_expectation", length = 100)
    private String salaryExpectation;

    @Column(name = "preferred_job_type", length = 50)
    private String preferredJobType;

    @Column(name = "preferred_work_location", length = 100)
    private String preferredWorkLocation;

    @Column(name = "willing_to_relocate")
    private Boolean willingToRelocate;

    @Column(name = "notice_period", length = 50)
    private String noticePeriod;

    @Column(name = "current_company", length = 100)
    private String currentCompany;

    @Column(name = "current_position", length = 100)
    private String currentPosition;

    @Column(name = "current_salary", length = 100)
    private String currentSalary;

    @Column(name = "experience")
    private Integer experience;

    @Column(name = "profile_rating")
    private Integer profileRating;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_active")
    private Boolean isActive;
}