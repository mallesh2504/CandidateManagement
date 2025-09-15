package com.CandidateManagement.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

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

public interface CandidateServiceInterface {

	ResponseEntity<CandidateResponseDto> addCandidates(CandidateRequestDto request)
			throws CandidateAlreadyExistException;

	ResponseEntity<CandidateLoginresponseDto> loginCandidate(CandidateLoginRequestDto loginRequest)
			throws CandidateNotFoundException, InvalidCredentialsException;

	ResponseEntity<CandidateResponseDto> getCandidate(Long candidateId) throws CandidateNotFoundException;

	List<CandidateResponseDto> getAllCandidates();

	List<CandidateResponseDto> filterCandidates(String source);

	List<CandidateResponseDto> filterCandidatesBylocation(String location);

	List<CandidateResponseDto> filterCandidatesBySkills(List<String> skills);

	ResponseEntity<CandidateResponseDto> updateCandidate(Long candidateId, CandidateUpdateRequestDto request)
			throws CandidateNotFoundException;

	ResponseEntity<CandidateDeleteResponse> deleteCandidate(Long candidateId) throws CandidateNotFoundException;

	ResponseEntity<CandidateContactResponseDto> contactSupport(CandidateContactRequestDto request)
			throws CandidateNotFoundException;

	ResponseEntity<CandidateDashboardDto> getCandidateDashboard(Long candidateId) throws CandidateNotFoundException;
}
