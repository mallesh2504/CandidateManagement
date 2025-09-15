package com.CandidateManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateContactResponseDto {

	private String candidateName;

	private String email;

	private String message;

}
