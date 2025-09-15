package com.CandidateManagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CandidateManagement.dto.ApplicationRequestDto;
import com.CandidateManagement.dto.ApplicationResponseDto;
import com.CandidateManagement.dto.ApplicationStatusUpdateRequestDto;
import com.CandidateManagement.exception.AlreadyAppliedException;
import com.CandidateManagement.exception.ApplicationNotFoundException;
import com.CandidateManagement.exception.CandidateNotFoundException;
import com.CandidateManagement.service.ApplicationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/applications")
@Tag(name = "Application Management", description = "APIs for managing job applications and tracking application status")
public class ApplicationController {

	@Autowired
	ApplicationService applicationService;

	@PostMapping()
	@Operation(summary = "Apply for a job", description = "Submit a new job application for a candidate")
	public ResponseEntity<ApplicationResponseDto> applyCandidateForJob(@RequestBody ApplicationRequestDto request)
			throws CandidateNotFoundException, AlreadyAppliedException {
		log.info("Received a Candidate Request : {} " ,request.getCandidateId()," To Apply the Job for : {}",request.getJobId());
		return applicationService.applyCandidateForJob(request);
	}

	@GetMapping("/{applicationId}/track")
	@Operation(summary = "Track application status", description = "Get the current status of a specific job application")
	public ResponseEntity<ApplicationResponseDto> getApplicationStatus(@PathVariable Long applicationId)
			throws ApplicationNotFoundException {
		log.info("Received a  Request to get application status : {} " ,applicationId);
		return applicationService.getApplicationStatus(applicationId);
	}

	@PutMapping("/status/{applicationId}")
	@Operation(summary = "Update application status", description = "Update the status of a job application (PENDING, REVIEWED, INTERVIEW, ACCEPTED, REJECTED)")
	public ResponseEntity<ApplicationResponseDto> updateApplicationStatus(@PathVariable Long applicationId,@RequestBody ApplicationStatusUpdateRequestDto request
			) throws ApplicationNotFoundException {
		log.info("Received a  Request to update application status : {} " ,applicationId);
		return applicationService.updateApplicationStatus(applicationId, request);
	}

	@GetMapping("ByCandidateId/{candidateId}")
	@Operation(summary = "Get candidate applications", description = "Retrieve all job applications submitted by a specific candidate")
	public ResponseEntity<List<ApplicationResponseDto>> listOfApplications(@PathVariable Long candidateId)
			throws ApplicationNotFoundException, CandidateNotFoundException {
		log.info("Received a  Candidate Request to get All Applications that belongs to : {} " ,candidateId);
		return applicationService.listOfApplications(candidateId);
	}
}