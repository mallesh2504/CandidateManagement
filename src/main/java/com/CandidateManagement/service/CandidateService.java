package com.CandidateManagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.CandidateManagement.dto.ApplicationResponseDto;
import com.CandidateManagement.dto.CandidateDashboardDto;
import com.CandidateManagement.dto.CandidateDeleteResponse;
import com.CandidateManagement.dto.CandidateLoginRequestDto;
import com.CandidateManagement.dto.CandidateLoginresponseDto;
import com.CandidateManagement.dto.CandidateRequestDto;
import com.CandidateManagement.dto.CandidateResponseDto;
import com.CandidateManagement.dto.CandidateUpdateRequestDto;
import com.CandidateManagement.dto.InterviewResponseDto;
import com.CandidateManagement.dto.OfferLetterResponseDto;
import com.CandidateManagement.dto.ResumeResponseDto;
import com.CandidateManagement.entity.CandidateEntity;
import com.CandidateManagement.exception.CandidateAlreadyExistException;
import com.CandidateManagement.exception.CandidateNotFoundException;
import com.CandidateManagement.exception.InvalidCredentialsException;
import com.CandidateManagement.repository.ApplicationRepository;
import com.CandidateManagement.repository.CandidateRepository;
import com.CandidateManagement.repository.ResumeRepository;
import com.CandidateManagement.util.JwtUtil;

@Service
public class CandidateService implements CandidateServiceInterface {

	@Autowired
	private CandidateRepository candidateRepo;

	@Autowired
	private ApplicationRepository applicationRepo1;

	@Autowired
	private ResumeRepository resumeRepo;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public ResponseEntity<CandidateResponseDto> addCandidates(CandidateRequestDto request)
			throws CandidateAlreadyExistException {

		Optional<CandidateEntity> existingCandidate = candidateRepo.findByEmail(request.getEmail());
		if (existingCandidate.isPresent()) {
			throw new CandidateAlreadyExistException("Candidate already exists, please login instead");
		}

		CandidateEntity candidate = new CandidateEntity();
		candidate.setFirstName(request.getFirstName());
		candidate.setLastName(request.getLastName());
		candidate.setEmail(request.getEmail());
		candidate.setPhone(request.getPhone());
		candidate.setSkills(request.getSkills());
		candidate.setPassword(request.getPassword());
		candidate.setSource(request.getSource());
		candidate.setLocation(request.getLocation());

		CandidateEntity saved = candidateRepo.save(candidate);
		CandidateResponseDto candidateDto = mapToResponseDto(saved);

		return new ResponseEntity<>(candidateDto, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<CandidateLoginresponseDto> loginCandidate(CandidateLoginRequestDto loginRequest)
			throws CandidateNotFoundException, InvalidCredentialsException {

		CandidateEntity candidate = candidateRepo.findByEmail(loginRequest.getEmail())
				.orElseThrow(() -> new CandidateNotFoundException("Candidate not found, please register first"));

		if (!candidate.getPassword().equals(loginRequest.getPassword())) {
			throw new InvalidCredentialsException("Invalid credentials, please try again");
		}

		String token = jwtUtil.generateToken(loginRequest.getEmail(), loginRequest.getPassword(),
				candidate.getFirstName(), candidate.getLastName());

		CandidateLoginresponseDto responseDto = new CandidateLoginresponseDto();
		responseDto.setEmail(loginRequest.getEmail());
		responseDto.setMessage("Successfully logged in with " + loginRequest.getEmail());
		responseDto.setJwtToken(token);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<CandidateResponseDto> getCandidate(Long candidateId) throws CandidateNotFoundException {
		CandidateEntity candidate = candidateRepo.findById(candidateId)
				.orElseThrow(() -> new CandidateNotFoundException("Candidate not found"));

		CandidateResponseDto candidateDto = mapToResponseDto(candidate);
		return new ResponseEntity<>(candidateDto, HttpStatus.OK);
	}

	@Override
	public List<CandidateResponseDto> getAllCandidates() {
		return candidateRepo.findAll().stream().map(this::mapToResponseDto).toList();
	}

	@Override
	public List<CandidateResponseDto> filterCandidates(String source) {
		return candidateRepo.findBySource(source).stream().map(this::mapToResponseDto).toList();
	}

	@Override
	public List<CandidateResponseDto> filterCandidatesBylocation(String location) {
		return candidateRepo.findByLocation(location).stream().map(this::mapToResponseDto).toList();
	}

	@Override
	public ResponseEntity<CandidateResponseDto> updateCandidate(Long candidateId, CandidateUpdateRequestDto request)
			throws CandidateNotFoundException {

		CandidateEntity candidate = candidateRepo.findById(candidateId)
				.orElseThrow(() -> new CandidateNotFoundException("Candidate not found"));

		candidate.setPhone(request.getPhone());
		candidate.setPassword(request.getPassword());
		candidate.setSkills(request.getSkills());
		candidate.setSource(request.getSource());
		candidate.setLocation(request.getLocation());

		CandidateEntity updated = candidateRepo.save(candidate);
		CandidateResponseDto responseDto = mapToResponseDto(updated);

		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<CandidateDeleteResponse> deleteCandidate(Long candidateId) throws CandidateNotFoundException {

		CandidateEntity candidate = candidateRepo.findById(candidateId)
				.orElseThrow(() -> new CandidateNotFoundException("Candidate not found"));

		CandidateDeleteResponse deleteResponse = new CandidateDeleteResponse();
		deleteResponse.setFirstName(candidate.getFirstName());
		deleteResponse.setLastName(candidate.getLastName());
		deleteResponse.setEmail(candidate.getEmail());
		deleteResponse.setPhone(candidate.getPhone());
		deleteResponse.setMessage("Candidate deleted successfully");

		candidateRepo.delete(candidate);
		return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<CandidateDashboardDto> getCandidateDashboard(Long candidateId)
			throws CandidateNotFoundException {

		CandidateEntity candidate = candidateRepo.findById(candidateId)
				.orElseThrow(() -> new CandidateNotFoundException("Candidate not found"));

		CandidateResponseDto profile = mapToResponseDto(candidate);

		ResumeResponseDto resume = null;
		if (candidate.getResume() != null) {
			resume = new ResumeResponseDto(candidate.getResume().getId(), candidate.getResume().getFileName(),
					candidate.getResume().getFileType());
		}

		List<ApplicationResponseDto> applications = applicationRepo1.findByCandidate_Id(candidateId).stream()
				.map(app -> new ApplicationResponseDto(app.getId(), candidateId, app.getJobId(), app.getStatus()))
				.toList();

		List<InterviewResponseDto> interviews = applicationRepo1.findByCandidate_Id(candidateId).stream()
				.filter(app -> app.getInterviewDetails() != null)
				.map(app -> new InterviewResponseDto(app.getId(), app.getInterviewDetails() 
				)).toList();

		List<OfferLetterResponseDto> offerLetters = applicationRepo1.findByCandidate_Id(candidateId).stream()
				.filter(app -> app.getOfferLetterUrl() != null)
				.map(app -> new OfferLetterResponseDto(app.getId(), app.getOfferLetterUrl())).toList();

		CandidateDashboardDto dashboard = new CandidateDashboardDto(profile, resume, applications, interviews,
				offerLetters);

		return new ResponseEntity<>(dashboard, HttpStatus.OK);
	}

	private CandidateResponseDto mapToResponseDto(CandidateEntity entity) {
		CandidateResponseDto dto = new CandidateResponseDto();
		dto.setFirstName(entity.getFirstName());
		dto.setLastName(entity.getLastName());
		dto.setEmail(entity.getEmail());
		dto.setPhone(entity.getPhone());
		dto.setSkills(entity.getSkills());
		dto.setSource(entity.getSource());
		dto.setLocation(entity.getLocation());

		if (entity.getResume() != null) {
			dto.setResumeFileName(entity.getResume().getFileName());
			dto.setResumeFileType(entity.getResume().getFileType());
			dto.setResumeDownloadUrl("/resume/download/" + entity.getResume().getId());
		}

		return dto;
	}
}
