package com.CandidateManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeduplicationResultDto {

	private Long candidateId;
	private boolean duplicateFound;
	private double similarityScore;
}
