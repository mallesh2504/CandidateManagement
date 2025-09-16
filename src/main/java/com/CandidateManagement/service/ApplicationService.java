package com.CandidateManagement.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.CandidateManagement.dto.ApplicationRequestDto;
import com.CandidateManagement.dto.ApplicationResponseDto;
import com.CandidateManagement.dto.ApplicationStatusUpdateRequestDto;
import com.CandidateManagement.entity.ApplicationEntity;
import com.CandidateManagement.entity.ApplicationStatus;
import com.CandidateManagement.entity.CandidateEntity;
import com.CandidateManagement.exception.AlreadyAppliedException;
import com.CandidateManagement.exception.ApplicationNotFoundException;
import com.CandidateManagement.exception.CandidateNotFoundException;
import com.CandidateManagement.repository.ApplicationRepository;
import com.CandidateManagement.repository.CandidateRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApplicationService implements ApplicationServiceInterface {

	@Autowired
	private ApplicationRepository applicationRepo;

	@Autowired
	private CandidateRepository candidateRepo;

	public ResponseEntity<ApplicationResponseDto> applyCandidateForJob(ApplicationRequestDto request)
			throws CandidateNotFoundException, AlreadyAppliedException {

		log.info("Processing job application request for candidate ID: {} and job ID: {}", request.getCandidateId(),
				request.getJobId());

		CandidateEntity candidate = candidateRepo.findById(request.getCandidateId()).orElseThrow(() -> {
			log.warn("Candidate not found with ID: {}", request.getCandidateId());
			return new CandidateNotFoundException("Candidate Not Found With Given Details, Please Register First.");
		});

		ApplicationEntity existingApplication = applicationRepo
				.findByCandidate_IdAndJobId(request.getCandidateId(), request.getJobId()).orElse(null);

		if (existingApplication != null) {
			log.warn("Application already exists for candidate ID: {} and job ID: {}", request.getCandidateId(),
					request.getJobId());
			throw new AlreadyAppliedException("You have already applied for this job.");
		}

		ApplicationEntity application = new ApplicationEntity();
		application.setCandidate(candidate);
		application.setJobId(request.getJobId());
		application.setStatus(request.getStatus() != null ? request.getStatus() : ApplicationStatus.APPLIED);
		application.setApplicationSource(request.getApplicationSource());
		application.setApplicationNotes(request.getApplicationNotes());
		application.setExpectedSalary(request.getExpectedSalary());
		application.setApplicationRating(request.getApplicationRating());
		application.setSkillMatchScore(request.getSkillMatchScore());
		application.setCreatedAt(LocalDateTime.now());

		ApplicationEntity saved = applicationRepo.save(application);

		log.info("Successfully created application with ID: {} for candidate: {} and job: {}", saved.getId(),
				candidate.getEmail(), request.getJobId());

		return new ResponseEntity<>(mapToDto(saved), HttpStatus.CREATED);
	}

	public ResponseEntity<ApplicationResponseDto> getApplicationStatus(Long applicationId)
			throws ApplicationNotFoundException {

		log.info("Retrieving application status for application ID: {}", applicationId);

		ApplicationEntity application = applicationRepo.findById(applicationId).orElseThrow(() -> {
			log.warn("Application not found with ID: {}", applicationId);
			return new ApplicationNotFoundException("With the Given Application Id is Not Found So please Apply First");
		});

		log.info("Successfully retrieved application status for ID: {} - Status: {}", applicationId,
				application.getStatus());

		return new ResponseEntity<>(mapToDto(application), HttpStatus.OK);
	}

	public ResponseEntity<ApplicationResponseDto> updateApplicationStatus(Long applicationId,
			ApplicationStatusUpdateRequestDto request) throws ApplicationNotFoundException {

		log.info("Updating application status for application ID: {} to status: {}", applicationId,
				request.getStatus());

		ApplicationEntity application = applicationRepo.findById(applicationId).orElseThrow(() -> {
			log.warn("Application not found with ID: {}", applicationId);
			return new ApplicationNotFoundException("Application not found for ID: " + applicationId);
		});

		log.debug("Found application with current status: {}, updating to: {}", application.getStatus(),
				request.getStatus());

		application.setStatus(request.getStatus());

		if (request.getApplicationStage() != null) {
			application.setApplicationStage(request.getApplicationStage());
		}

		if (request.getIsActive() != null) {
			application.setIsActive(request.getIsActive());
		}

		ApplicationEntity saved = applicationRepo.save(application);

		log.info("Successfully updated application ID: {} status from {} to {}", applicationId, application.getStatus(),
				request.getStatus());

		return new ResponseEntity<>(mapToDto(saved), HttpStatus.ACCEPTED);
	}

	@Override
	public ResponseEntity<List<ApplicationResponseDto>> listOfApplications(Long candidateId)
			throws CandidateNotFoundException, ApplicationNotFoundException {

		log.info("Retrieving applications list for candidate ID: {}", candidateId);

		CandidateEntity candidate = candidateRepo.findById(candidateId).orElseThrow(() -> {
			log.warn("Candidate not found with ID: {}", candidateId);
			return new CandidateNotFoundException("Candidate not found");
		});

		log.debug("Candidate found: {}, retrieving applications", candidate.getEmail());
		List<ApplicationEntity> applications = applicationRepo.findByCandidate_Id(candidateId);

		if (applications.isEmpty()) {
			log.warn("No applications found for candidate ID: {}", candidateId);
			throw new ApplicationNotFoundException("No applications found for this candidate");
		}

		List<ApplicationResponseDto> responseDtos = applications.stream().map(this::mapToDto).toList();

		log.info("Successfully retrieved {} applications for candidate ID: {}", responseDtos.size(), candidateId);

		return ResponseEntity.ok(responseDtos);
	}

	private ApplicationResponseDto mapToDto(ApplicationEntity app) {
		ApplicationResponseDto dto = new ApplicationResponseDto();
		dto.setId(app.getId());
		dto.setCandidateId(app.getCandidate().getId());
		dto.setJobId(app.getJobId());
		dto.setStatus(app.getStatus());
		dto.setApplicationStage(app.getApplicationStage());
		dto.setApplicationSource(app.getApplicationSource());
		dto.setApplicationNotes(app.getApplicationNotes());
		dto.setExpectedSalary(app.getExpectedSalary());
		dto.setApplicationRating(app.getApplicationRating());
		dto.setSkillMatchScore(app.getSkillMatchScore());
		dto.setCreatedAt(app.getCreatedAt());
		dto.setIsActive(app.getIsActive());
		return dto;
	}
}