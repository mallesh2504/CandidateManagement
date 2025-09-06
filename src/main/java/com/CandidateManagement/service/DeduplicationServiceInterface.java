package com.CandidateManagement.service;

import org.springframework.http.ResponseEntity;

import com.CandidateManagement.dto.DeduplicationResultDto;
import com.CandidateManagement.exception.CandidateNotFoundException;

public interface DeduplicationServiceInterface {

	public ResponseEntity<DeduplicationResultDto> checkDuplicate(Long candidateId)throws CandidateNotFoundException;
}
