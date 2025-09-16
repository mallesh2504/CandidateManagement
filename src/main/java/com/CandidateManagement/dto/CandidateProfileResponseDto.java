package com.CandidateManagement.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateProfileResponseDto {

	private Long id;
	private Long candidateId;
	private String linkedinUrl;
	private String githubUrl;
	private String portfolioUrl;
	private String websiteUrl;
	private String bio;
	private String summary;
	private LocalDateTime dateOfBirth;
	private String gender;
	private String nationality;
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
	private LocalDateTime createdAt;
	private Boolean isActive;
}