package com.CandidateManagement.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.CandidateManagement.dto.ApplicationRequestDto;
import com.CandidateManagement.dto.ApplicationResponseDto;
import com.CandidateManagement.dto.ApplicationStatusUpdateRequestDto;
import com.CandidateManagement.exception.AlreadyAppliedException;
import com.CandidateManagement.exception.ApplicationNotFoundException;
import com.CandidateManagement.exception.CandidateNotFoundException;

public interface ApplicationServiceInterface {

	ResponseEntity<ApplicationResponseDto> applyCandidateForJob(ApplicationRequestDto request)
			throws CandidateNotFoundException, AlreadyAppliedException;

	ResponseEntity<ApplicationResponseDto> getApplicationStatus(Long applicationId) throws ApplicationNotFoundException;

	ResponseEntity<ApplicationResponseDto> updateApplicationStatus(Long applicationId,
			ApplicationStatusUpdateRequestDto request) throws ApplicationNotFoundException;

	ResponseEntity<List<ApplicationResponseDto>> listOfApplications(Long candidateId)
			throws CandidateNotFoundException, ApplicationNotFoundException;
}
