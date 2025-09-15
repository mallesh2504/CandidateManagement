package com.CandidateManagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CandidateManagement.dto.CandidateContactRequestDto;
import com.CandidateManagement.dto.CandidateContactResponseDto;
import com.CandidateManagement.dto.CandidateDashboardDto;
import com.CandidateManagement.dto.CandidateDeleteResponse;
import com.CandidateManagement.dto.CandidateLoginRequestDto;
import com.CandidateManagement.dto.CandidateLoginresponseDto;
import com.CandidateManagement.dto.CandidateRequestDto;
import com.CandidateManagement.dto.CandidateResponseDto;
import com.CandidateManagement.dto.CandidateUpdateRequestDto;
import com.CandidateManagement.exception.CandidateAlreadyExistException;
import com.CandidateManagement.exception.CandidateNotFoundException;
import com.CandidateManagement.exception.InvalidCredentialsException;
import com.CandidateManagement.service.CandidateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/candidates")
@Tag(name = "Candidate Management", description = "APIs for managing candidate profiles, authentication, and filtering")
public class CandidateController {

	@Autowired
	private CandidateService candidateService;

	@PostMapping()
	@Operation(summary = "Register new candidate", description = "Create a new candidate profile in the system")
	public ResponseEntity<CandidateResponseDto> addCandidates(@RequestBody CandidateRequestDto request)
			throws CandidateAlreadyExistException {
		log.info("Received candidate registration request for email: {}", request.getEmail());
		return candidateService.addCandidates(request);
	}

	@GetMapping("/{candidateId}")
	@Operation(summary = "Get candidate by ID", description = "Retrieve candidate details by candidate ID")
	public ResponseEntity<CandidateResponseDto> getCandidate(@PathVariable Long candidateId)
			throws CandidateNotFoundException {
		log.info("Received Candidate request to get Candidate Details: {}",candidateId);
		return candidateService.getCandidate(candidateId);
	}

	@GetMapping("/{candidateId}/dashboard")
	@Operation(summary = "Get candidate dashboard", description = "Retrieve comprehensive candidate dashboard with profile, applications, and documents")
	public ResponseEntity<CandidateDashboardDto> getCandidateDashboard(@PathVariable Long candidateId)
			throws CandidateNotFoundException {
		log.info("Received Candidate request to get Candidate Dashboard : {}",candidateId);
		return candidateService.getCandidateDashboard(candidateId);
	}

	@PostMapping("/login")
	@Operation(summary = "Candidate login", description = "Authenticate candidate and return JWT token")
	public ResponseEntity<CandidateLoginresponseDto> loginCandidate(@RequestBody CandidateLoginRequestDto loginRequest)
			throws CandidateNotFoundException, InvalidCredentialsException {
		log.info("Received Candidate Login request with this: {}",loginRequest.getEmail());
		return candidateService.loginCandidate(loginRequest);
	}

	@GetMapping("/getAll")
	@Operation(summary = "Get all candidates", description = "Retrieve list of all candidates in the system")
	public List<CandidateResponseDto> getAllCandidates() {
		log.info("Received a User request to Get All Candidates");
		return candidateService.getAllCandidates();
	}

	@GetMapping("/filter/source/{source}")
	@Operation(summary = "Filter candidates by source", description = "Get candidates filtered by their recruitment source (LinkedIn, Indeed, etc.)")
	public List<CandidateResponseDto> filterCandidates(@PathVariable String source) {
		log.info("Received a User request to Get All Candidates that belongs to this source : {}",source);
		return candidateService.filterCandidates(source);
	}

	@GetMapping("/filter/location/{location}")
	@Operation(summary = "Filter candidates by location", description = "Get candidates filtered by their geographical location")
	public List<CandidateResponseDto> filterCandidatesBylocation(@PathVariable String location) {
		log.info("Received a User request to Get All Candidates that belongs to this location : {}",location);
		return candidateService.filterCandidatesBylocation(location);
	}

	@GetMapping("/filter/skills")
	@Operation(summary = "Filter candidates by skills", description = "Get candidates who possess any of the specified skills (case-insensitive matching)")
	public List<CandidateResponseDto> filterCandidatesBySkills(@RequestBody List<String> skills) {
		log.info("Received a User request to Get All Candidates that belongs to this skills : {}",skills);
		return candidateService.filterCandidatesBySkills(skills);
	}

	@PutMapping("/{candidateId}")
	@Operation(summary = "Update candidate profile", description = "Update candidate information and profile details")
	public ResponseEntity<CandidateResponseDto> updateCandidate(@PathVariable Long candidateId,
			@RequestBody CandidateUpdateRequestDto request) throws CandidateNotFoundException {
		log.info("Received a Candidate request to Update Details of : {}",candidateId);
		return candidateService.updateCandidate(candidateId, request);
	}

	@DeleteMapping("/{candidateId}")
	@Operation(summary = "Delete candidate", description = "Remove candidate from the system permanently")
	public ResponseEntity<CandidateDeleteResponse> deleteCandidate(@PathVariable Long candidateId)
			throws CandidateNotFoundException {
		log.info("Received a Candidate request to Delete Account : {}",candidateId);
		return candidateService.deleteCandidate(candidateId);
	}
	
	@PostMapping("/contactSupport")
	public ResponseEntity<CandidateContactResponseDto> contactSupport(@RequestBody CandidateContactRequestDto request) throws CandidateNotFoundException{
		return candidateService.contactSupport(request);
		
	}
}