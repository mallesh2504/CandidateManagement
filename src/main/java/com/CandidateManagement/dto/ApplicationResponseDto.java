package com.CandidateManagement.dto;

import com.CandidateManagement.entity.ApplicationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponseDto {

	private Long id;
	private Long candidateId;
	private Long jobId;
	private ApplicationStatus status;

}
