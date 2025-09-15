package com.CandidateManagement.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.CandidateManagement.dto.CandidateContactRequestDto;
import com.CandidateManagement.dto.CandidateContactResponseDto;
import com.CandidateManagement.dto.CandidateDashboardDto;
import com.CandidateManagement.dto.CandidateDeleteResponse;
import com.CandidateManagement.dto.CandidateLoginRequestDto;
import com.CandidateManagement.dto.CandidateLoginresponseDto;
import com.CandidateManagement.dto.CandidateRequestDto;
import com.CandidateManagement.dto.CandidateResponseDto;
import com.CandidateManagement.dto.CandidateUpdateRequestDto;
import com.CandidateManagement.entity.CandidateEntity;
import com.CandidateManagement.entity.ContactSupportEntity;
import com.CandidateManagement.exception.CandidateAlreadyExistException;
import com.CandidateManagement.exception.CandidateNotFoundException;
import com.CandidateManagement.exception.InvalidCredentialsException;
import com.CandidateManagement.repository.ApplicationRepository;
import com.CandidateManagement.repository.CandidateRepository;
import com.CandidateManagement.repository.ContactSupportRepository;
import com.CandidateManagement.repository.InterviewRepository;
import com.CandidateManagement.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CandidateService implements CandidateServiceInterface {

	@Autowired
	private CandidateRepository candidateRepo;

	@Autowired
	private ApplicationRepository applicationRepo;

	@Autowired
	private InterviewRepository interviewRepo;
	
	@Autowired
	private ContactSupportRepository contactSupportRepo;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public ResponseEntity<CandidateResponseDto> addCandidates(CandidateRequestDto request)
			throws CandidateAlreadyExistException {

		log.info("Processing candidate registration request for email: {}", request.getEmail());

		List<CandidateEntity> existingCandidates = candidateRepo.findByEmailOrPhone(request.getEmail(),
				request.getPhone());

		if (!existingCandidates.isEmpty()) {
			log.debug("Found {} existing candidates to check for duplicates", existingCandidates.size());

			for (CandidateEntity existing : existingCandidates) {
				if (existing.getEmail().equals(request.getEmail()) && existing.getPhone().equals(request.getPhone())) {
					log.warn("Registration failed - candidate already exists with both email: {} and phone: {}",
							request.getEmail(), request.getPhone());
					throw new CandidateAlreadyExistException(
							"Candidate already exists with this email and phone number, please login instead");
				} else if (existing.getEmail().equals(request.getEmail())) {
					log.warn("Registration failed - candidate already exists with email: {}", request.getEmail());
					throw new CandidateAlreadyExistException(
							"Candidate already exists with this email address, please login instead");
				} else if (existing.getPhone().equals(request.getPhone())) {
					log.warn("Registration failed - candidate already exists with phone: {}", request.getPhone());
					throw new CandidateAlreadyExistException(
							"Candidate already exists with this phone number, please login instead");
				}
			}
		}

		log.debug("Creating new candidate entity with details: firstName={}, lastName={}, email={}, phone={}",
				request.getFirstName(), request.getLastName(), request.getEmail(), request.getPhone());

		CandidateEntity candidate = new CandidateEntity();
		candidate.setFirstName(request.getFirstName());
		candidate.setLastName(request.getLastName());
		candidate.setEmail(request.getEmail());
		candidate.setPhone(request.getPhone());
		candidate.setPassword(request.getPassword());

		if (request.getSkills() != null && !request.getSkills().isEmpty()) {
			candidate.setSkills(String.join(",", request.getSkills()));
		}

		candidate.setSource(request.getSource());
		candidate.setLocation(request.getLocation());

		CandidateEntity saved = candidateRepo.save(candidate);

		log.info("Successfully registered candidate with ID: {} and email: {}", saved.getId(), saved.getEmail());

		CandidateResponseDto candidateDto = mapToResponseDto(saved);

		return new ResponseEntity<>(candidateDto, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<CandidateLoginresponseDto> loginCandidate(CandidateLoginRequestDto loginRequest)
			throws CandidateNotFoundException, InvalidCredentialsException {

		log.info("Processing login request for email: {}", loginRequest.getEmail());

		CandidateEntity candidate = candidateRepo.findByEmail(loginRequest.getEmail()).orElseThrow(() -> {
			log.warn("Login failed - candidate not found with email: {}", loginRequest.getEmail());
			return new CandidateNotFoundException("Candidate not found, please register first");
		});

		log.debug("Candidate found, validating password for email: {}", loginRequest.getEmail());
		if (!candidate.getPassword().equals(loginRequest.getPassword())) {
			log.warn("Login failed - invalid password for email: {}", loginRequest.getEmail());
			throw new InvalidCredentialsException("Invalid credentials, please try again");
		}

		log.debug("Generating JWT token for candidate: {}", candidate.getEmail());
		String token = jwtUtil.generateToken(loginRequest.getEmail(), loginRequest.getPassword(),
				candidate.getFirstName(), candidate.getLastName());

		CandidateLoginresponseDto responseDto = new CandidateLoginresponseDto();
		responseDto.setEmail(loginRequest.getEmail());
		responseDto.setMessage("Successfully logged in with " + loginRequest.getEmail());
		responseDto.setJwtToken(token);

		log.info("Successfully logged in candidate with email: {}", loginRequest.getEmail());
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<CandidateResponseDto> getCandidate(Long candidateId) throws CandidateNotFoundException {
		log.info("Retrieving candidate details for ID: {}", candidateId);

		CandidateEntity candidate = candidateRepo.findById(candidateId).orElseThrow(() -> {
			log.warn("Candidate not found with ID: {}", candidateId);
			return new CandidateNotFoundException("Candidate not found");
		});

		log.info("Successfully retrieved candidate: {} with ID: {}", candidate.getEmail(), candidateId);
		CandidateResponseDto candidateDto = mapToResponseDto(candidate);
		return new ResponseEntity<>(candidateDto, HttpStatus.OK);
	}

	@Override
	public List<CandidateResponseDto> getAllCandidates() {
		log.info("Retrieving all candidates");

		List<CandidateEntity> candidates = candidateRepo.findAll();

		List<CandidateResponseDto> responseDtos = candidates.stream().map(this::mapToResponseDto).toList();

		return responseDtos;
	}

	@Override
	public List<CandidateResponseDto> filterCandidates(String source) {
		log.info("Filtering candidates by source: {}", source);

		List<CandidateEntity> candidates = candidateRepo.findBySource(source);

		List<CandidateResponseDto> responseDtos = candidates.stream().map(this::mapToResponseDto).toList();

		return responseDtos;
	}

	@Override
	public List<CandidateResponseDto> filterCandidatesBylocation(String location) {
		log.info("Filtering candidates by location: {}", location);

		List<CandidateEntity> candidates = candidateRepo.findByLocation(location);

		List<CandidateResponseDto> responseDtos = candidates.stream().map(this::mapToResponseDto).toList();

		return responseDtos;
	}

	@Override
	public List<CandidateResponseDto> filterCandidatesBySkills(List<String> skills) {
		log.info("Filtering candidates by skills: {}", skills);

		if (skills == null || skills.isEmpty()) {
			log.warn("No skills provided for filtering, returning empty list");
			return List.of();
		}

		List<CandidateEntity> matchedCandidates = new ArrayList<>();
		for (String skill : skills) {
			if (skill != null && !skill.trim().isEmpty()) {
				log.debug("Searching for candidates with skill: {}", skill);
				List<CandidateEntity> skillCandidates = candidateRepo.findBySkill(skill.toLowerCase());
				matchedCandidates.addAll(skillCandidates);
			}
		}

		List<CandidateResponseDto> responseDtos = matchedCandidates.stream().map(this::mapToResponseDto).toList();

		return responseDtos;
	}

	@Override
	public ResponseEntity<CandidateResponseDto> updateCandidate(Long candidateId, CandidateUpdateRequestDto request)
			throws CandidateNotFoundException {

		log.info("Updating candidate with ID: {}", candidateId);

		CandidateEntity candidate = candidateRepo.findById(candidateId).orElseThrow(() -> {
			log.warn("Update failed - candidate not found with ID: {}", candidateId);
			return new CandidateNotFoundException("Candidate not found");
		});

		candidate.setPhone(request.getPhone());
		candidate.setPassword(request.getPassword());

		// Convert List -> CSV
		if (request.getSkills() != null && !request.getSkills().isEmpty()) {
			candidate.setSkills(String.join(",", request.getSkills()));
		}

		candidate.setSource(request.getSource());
		candidate.setLocation(request.getLocation());
		CandidateEntity updated = candidateRepo.save(candidate);

		log.info("Successfully updated candidate with ID: {} and email: {}", updated.getId(), updated.getEmail());

		CandidateResponseDto responseDto = mapToResponseDto(updated);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<CandidateDeleteResponse> deleteCandidate(Long candidateId) throws CandidateNotFoundException {
		log.info("Deleting candidate with ID: {}", candidateId);

		CandidateEntity candidate = candidateRepo.findById(candidateId).orElseThrow(() -> {
			log.warn("Delete failed - candidate not found with ID: {}", candidateId);
			return new CandidateNotFoundException("Candidate not found");
		});

		CandidateDeleteResponse deleteResponse = new CandidateDeleteResponse();
		deleteResponse.setFirstName(candidate.getFirstName());
		deleteResponse.setLastName(candidate.getLastName());
		deleteResponse.setEmail(candidate.getEmail());
		deleteResponse.setPhone(candidate.getPhone());
		deleteResponse.setMessage("Candidate deleted successfully");

		candidateRepo.delete(candidate);

		log.info("Successfully deleted candidate with ID: {} and email: {}", candidateId, candidate.getEmail());

		return new ResponseEntity<>(deleteResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<CandidateDashboardDto> getCandidateDashboard(Long candidateId)
			throws CandidateNotFoundException {

		CandidateEntity candidate = candidateRepo.findById(candidateId)
				.orElseThrow(() -> new CandidateNotFoundException("Candidate not found"));

		CandidateResponseDto profile = mapToResponseDto(candidate);

		List<CandidateDashboardDto.ApplicationDto> applications = applicationRepo.findByCandidate_Id(candidateId)
				.stream().map(app -> new CandidateDashboardDto.ApplicationDto(app.getId(), app.getJobId(),
						app.getStatus().toString()))
				.toList();

		List<CandidateDashboardDto.DocumentDto> documents = candidate.getDocuments() != null
				? candidate.getDocuments().stream()
						.map(doc -> new CandidateDashboardDto.DocumentDto(doc.getId(), doc.getFileName(),
								doc.getFileType(), doc.getS3Url(), doc.getDocumentType().toString(),
								doc.getUploadedAt() != null ? doc.getUploadedAt().toString() : null))
						.toList()
				: List.of();

		List<CandidateDashboardDto.InterviewDto> interviews = interviewRepo.findByApplication_Candidate_Id(candidateId)
				.stream()
				.map(interview -> new CandidateDashboardDto.InterviewDto(interview.getId(),
						interview.getApplication().getId(), interview.getInterviewType(), interview.getInterviewStage(),
						interview.getScheduledDate(), interview.getDurationMinutes(), interview.getLocation(),
						interview.getMeetingLink(), interview.getInterviewNotes(), interview.getInterviewStatus(),
						interview.getIsActive()))
				.toList();

		CandidateDashboardDto dashboard = new CandidateDashboardDto(profile, applications, documents, interviews);

		return new ResponseEntity<>(dashboard, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<CandidateContactResponseDto> contactSupport(CandidateContactRequestDto request)
			throws CandidateNotFoundException {

		CandidateEntity candidate = candidateRepo.findById(request.getCandidateId())
				.orElseThrow(() -> new CandidateNotFoundException("Candidate not found"));

		ContactSupportEntity entity = new ContactSupportEntity();
		entity.setCandidate(candidate);
		entity.setCandidateName(request.getCandidateName());
		entity.setEmail(request.getEmail());
		entity.setMessage(request.getMessage());

		
		contactSupportRepo.save(entity);

		CandidateContactResponseDto dto = new CandidateContactResponseDto();
		dto.setCandidateName(entity.getCandidateName());
		dto.setEmail(entity.getEmail());
		dto.setMessage(entity.getMessage());

		return new ResponseEntity<>(dto, HttpStatus.CREATED);
	}

	private CandidateResponseDto mapToResponseDto(CandidateEntity entity) {
		CandidateResponseDto dto = new CandidateResponseDto();
		dto.setFirstName(entity.getFirstName());
		dto.setLastName(entity.getLastName());
		dto.setEmail(entity.getEmail());
		dto.setPhone(entity.getPhone());
		dto.setSource(entity.getSource());
		dto.setLocation(entity.getLocation());

		if (entity.getSkills() != null && !entity.getSkills().isEmpty()) {
			dto.setSkills(Arrays.asList(entity.getSkills().split(",")));
		} else {
			dto.setSkills(List.of());
		}

		return dto;
	}

}
