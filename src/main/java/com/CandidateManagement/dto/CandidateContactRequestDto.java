package com.CandidateManagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateContactRequestDto {

	@NotNull(message = "Candidate ID is required")
	private Long candidateId;

	@NotNull(message = "Candidate name is required")
	@NotBlank(message = "Candidate name cannot be blank")
	private String candidateName;

	@NotNull(message = "Email is required")
	@NotBlank(message = "Email cannot be blank")
	private String email;

	@NotNull(message = "Message is required")
	@NotBlank(message = "Message cannot be blank")
	private String message;

}
