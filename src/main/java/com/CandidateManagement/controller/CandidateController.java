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

@RestController
@RequestMapping("/candidates")
public class CandidateController {

	@Autowired
	private CandidateService candidateService;

	@PostMapping("/add")
	public ResponseEntity<CandidateResponseDto> addCandidates(@RequestBody CandidateRequestDto request)
			throws CandidateAlreadyExistException {
		return candidateService.addCandidates(request);
	}

	@GetMapping("/get/{candidateId}")
	public ResponseEntity<CandidateResponseDto> getCandidate(@PathVariable Long candidateId)
			throws CandidateNotFoundException {
		return candidateService.getCandidate(candidateId);
	}

	@GetMapping("/dashboard/{candidateId}")
	public ResponseEntity<CandidateDashboardDto> getCandidateDashboard(@PathVariable Long candidateId)
			throws CandidateNotFoundException {
		return candidateService.getCandidateDashboard(candidateId);
	}

	@PostMapping("/login")
	public ResponseEntity<CandidateLoginresponseDto> loginCandidate(@RequestBody CandidateLoginRequestDto loginRequest)
			throws CandidateNotFoundException, InvalidCredentialsException {
		return candidateService.loginCandidate(loginRequest);
	}

	@GetMapping("/getAll")
	public List<CandidateResponseDto> getAllCandidates() {
		return candidateService.getAllCandidates();
	}

	@GetMapping("/filter/{source}")
	public List<CandidateResponseDto> filterCandidates(@PathVariable String source) {
		return candidateService.filterCandidates(source);
	}

	@GetMapping("/filterByLocation/{location}")
	public List<CandidateResponseDto> filterCandidatesBylocation(@PathVariable String location) {
		return candidateService.filterCandidatesBylocation(location);
	}

	@PutMapping("/update/{candidateId}")
	public ResponseEntity<CandidateResponseDto> updateCandidate(@PathVariable Long candidateId,
			@RequestBody CandidateUpdateRequestDto request) throws CandidateNotFoundException {
		return candidateService.updateCandidate(candidateId, request);
	}

	@DeleteMapping("/deleteById/{candidateId}")
	public ResponseEntity<CandidateDeleteResponse> deleteCandidate(@PathVariable Long candidateId)
			throws CandidateNotFoundException {
		return candidateService.deleteCandidate(candidateId);
	}
}
