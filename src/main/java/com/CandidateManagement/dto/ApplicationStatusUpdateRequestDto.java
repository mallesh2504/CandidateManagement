package com.CandidateManagement.dto;

import com.CandidateManagement.entity.ApplicationStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationStatusUpdateRequestDto {

	@NotNull(message = "Application ID is required")
	private Long applicationId;

	@NotNull(message = "Status is required")
	private ApplicationStatus status;

}
