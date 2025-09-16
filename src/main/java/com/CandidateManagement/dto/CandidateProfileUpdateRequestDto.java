package com.CandidateManagement.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateProfileUpdateRequestDto {

    private String linkedinUrl;
    private String githubUrl;
    private String portfolioUrl;
    private String websiteUrl;
    private String bio;
    private String summary;
    private String availabilityStatus;
    private Timestamp availabilityDate;
    private String salaryExpectation;
    private String preferredJobType;
    private String preferredWorkLocation;
    private Boolean willingToRelocate;
    private String noticePeriod;
    private String currentCompany;
    private String currentPosition;
    private String currentSalary;
    private Integer experience;
    private Integer profileRating;
    private Boolean isActive;
}