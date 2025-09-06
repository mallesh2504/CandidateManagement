package com.CandidateManagement.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CandidateDashboardDto {

	private CandidateResponseDto profile;
	private ResumeResponseDto resume;
	private List<ApplicationResponseDto> applications;
	private List<InterviewResponseDto> interviews;
	private List<OfferLetterResponseDto> offerLetters;
}
