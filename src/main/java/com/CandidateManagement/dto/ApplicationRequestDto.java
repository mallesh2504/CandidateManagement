package com.CandidateManagement.dto;

import com.CandidateManagement.entity.ApplicationStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRequestDto {
	private Long candidateId;
	private Long jobId;
	private ApplicationStatus status;
}
