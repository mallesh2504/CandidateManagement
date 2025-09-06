package com.CandidateManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.CandidateManagement.dto.*;
import com.CandidateManagement.entity.ApplicationStatus;
import com.CandidateManagement.exception.*;
import com.CandidateManagement.service.ApplicationService;

@RestController
@RequestMapping("/application")
public class ApplicationController {

	@Autowired
	ApplicationService applicationService;

	@PostMapping("/apply")
	public ResponseEntity<ApplicationResponseDto> applyCandidateForJob(@RequestBody ApplicationRequestDto request)
			throws CandidateNotFoundException, AlreadyAppliedException {
		return applicationService.applyCandidateForJob(request);
	}

	@GetMapping("/track/{applicationId}")
	public ResponseEntity<ApplicationResponseDto> getApplicationStatus(@PathVariable Long applicationId)
			throws ApplicationNotFoundException {
		return applicationService.getApplicationStatus(applicationId);
	}

	@PutMapping("/updateStatus/{applicationId}/{status}/{interviewDetails}")
	public ResponseEntity<ApplicationResponseDto> updateApplicationStatus(@PathVariable Long applicationId,
			@PathVariable ApplicationStatus status, @PathVariable String interviewDetails)
			throws ApplicationNotFoundException {
		return applicationService.updateApplicationStatus(applicationId, status,interviewDetails);
	}
	
	@PutMapping("/updateOfferLetter")
	public ResponseEntity<OfferLetterResponseDto> updateofferLetterUrl(@RequestBody OfferLetterRequestDto request)
			throws ApplicationNotFoundException {
		return applicationService.updateofferLetterUrl(request);
	}

	@GetMapping("/interviews/{applicationId}")
	public ResponseEntity<InterviewResponseDto> getInterviewInvites(@PathVariable Long applicationId)
			throws ApplicationNotFoundException {
		return applicationService.getInterviewInvites(applicationId);
	}

	@GetMapping("/download-offer-letter/{applicationId}")
	public ResponseEntity<OfferLetterResponseDto> downloadOfferLetter(@PathVariable Long applicationId)
			throws ApplicationNotFoundException {
		return applicationService.downloadOfferLetter(applicationId);
	}
}
