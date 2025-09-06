package com.CandidateManagement.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.CandidateManagement.dto.ApplicationRequestDto;
import com.CandidateManagement.dto.ApplicationResponseDto;
import com.CandidateManagement.dto.InterviewResponseDto;
import com.CandidateManagement.dto.OfferLetterRequestDto;
import com.CandidateManagement.dto.OfferLetterResponseDto;
import com.CandidateManagement.entity.ApplicationEntity;
import com.CandidateManagement.entity.ApplicationStatus;
import com.CandidateManagement.entity.CandidateEntity;
import com.CandidateManagement.exception.AlreadyAppliedException;
import com.CandidateManagement.exception.ApplicationNotFoundException;
import com.CandidateManagement.exception.CandidateNotFoundException;
import com.CandidateManagement.repository.ApplicationRepository;
import com.CandidateManagement.repository.CandidateRepository;

@Service
public class ApplicationService implements ApplicationServiceInterface {

	@Autowired
	private ApplicationRepository applicationRepo;

	@Autowired
	private CandidateRepository candidateRepo;

	public ResponseEntity<ApplicationResponseDto> applyCandidateForJob(ApplicationRequestDto request)
			throws CandidateNotFoundException, AlreadyAppliedException {

		CandidateEntity candidate = candidateRepo.findById(request.getCandidateId()).orElseThrow(
				() -> new CandidateNotFoundException("Candidate Not Found With Given Details, Please Register First."));

		ApplicationEntity existingApplication = applicationRepo
				.findByCandidate_IdAndJobId(request.getCandidateId(), request.getJobId()).orElse(null);

		if (existingApplication != null) {
			throw new AlreadyAppliedException("You have already applied for this job.");
		}

		ApplicationEntity application = new ApplicationEntity();
		application.setCandidate(candidate);
		application.setJobId(request.getJobId());
		application.setStatus(request.getStatus() != null ? request.getStatus() : ApplicationStatus.APPLIED);

		ApplicationEntity saved = applicationRepo.save(application);

		return new ResponseEntity<>(mapToDto(saved), HttpStatus.CREATED);
	}

	public ResponseEntity<OfferLetterResponseDto> updateofferLetterUrl(OfferLetterRequestDto request)
			throws ApplicationNotFoundException {
		Optional<ApplicationEntity> appliactionEntity = applicationRepo.findById(request.getApplicationId());
		if (appliactionEntity.isEmpty()) {
			throw new ApplicationNotFoundException("Application not found for ID: " + request.getApplicationId());
		}

		ApplicationEntity application = appliactionEntity.get();

		application.setOfferLetterUrl(request.getOfferLetterUrl());

		applicationRepo.save(application);

		OfferLetterResponseDto letterdto = new OfferLetterResponseDto();

		letterdto.setApplicationId(request.getApplicationId());
		letterdto.setOfferLetterUrl(request.getOfferLetterUrl());

		return new ResponseEntity<>(letterdto, HttpStatus.ACCEPTED);
	}

	public ResponseEntity<ApplicationResponseDto> getApplicationStatus(Long applicationId)
			throws ApplicationNotFoundException {

		ApplicationEntity application = applicationRepo.findById(applicationId)
				.orElseThrow(() -> new ApplicationNotFoundException(
						"With the Given Application Id is Not Found So please Apply First"));

		return new ResponseEntity<>(mapToDto(application), HttpStatus.OK); // ✅ fixed from FOUND → OK
	}

	public ResponseEntity<InterviewResponseDto> getInterviewInvites(Long applicationId)
			throws ApplicationNotFoundException {

		ApplicationEntity application = applicationRepo.findById(applicationId)
				.orElseThrow(() -> new ApplicationNotFoundException("Application not found for ID: " + applicationId));

		if (application.getInterviewDetails() == null || application.getInterviewDetails().isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(new InterviewResponseDto(applicationId, "No interview invites available yet."));
		}

		return ResponseEntity.ok(new InterviewResponseDto(applicationId, application.getInterviewDetails()));
	}

	public ResponseEntity<OfferLetterResponseDto> downloadOfferLetter(Long applicationId)
			throws ApplicationNotFoundException {

		ApplicationEntity application = applicationRepo.findById(applicationId)
				.orElseThrow(() -> new ApplicationNotFoundException("Application not found for ID: " + applicationId));

		if (application.getOfferLetterUrl() == null || application.getOfferLetterUrl().isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(new OfferLetterResponseDto(applicationId, "Offer letter not available yet."));
		}

		return ResponseEntity.ok(new OfferLetterResponseDto(applicationId, application.getOfferLetterUrl()));
	}

	public ResponseEntity<ApplicationResponseDto> updateApplicationStatus(Long applicationId, ApplicationStatus status,
			String interviewDetails) throws ApplicationNotFoundException {

		ApplicationEntity application = applicationRepo.findById(applicationId)
				.orElseThrow(() -> new ApplicationNotFoundException("Application not found for ID: " + applicationId));

		application.setStatus(status);
		application.setInterviewDetails(interviewDetails);

		ApplicationEntity saved = applicationRepo.save(application);

		return new ResponseEntity<>(mapToDto(saved), HttpStatus.ACCEPTED);
	}

	private ApplicationResponseDto mapToDto(ApplicationEntity app) {
		return new ApplicationResponseDto(app.getId(), app.getCandidate().getId(), app.getJobId(), app.getStatus());
	}

}
