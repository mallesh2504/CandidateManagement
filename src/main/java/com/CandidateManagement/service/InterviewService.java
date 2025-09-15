package com.CandidateManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.CandidateManagement.dto.InterviewScheduleRequestDto;
import com.CandidateManagement.dto.InterviewScheduleResponseDto;
import com.CandidateManagement.dto.InterviewScheduleUpdateRequestDto;
import com.CandidateManagement.entity.ApplicationEntity;
import com.CandidateManagement.entity.InterviewEntity;
import com.CandidateManagement.exception.ApplicationNotFoundException;
import com.CandidateManagement.exception.InterviewNotFoundException;
import com.CandidateManagement.repository.ApplicationRepository;
import com.CandidateManagement.repository.InterviewRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InterviewService implements InterviewServiceInterface {

	@Autowired
	private InterviewRepository interviewRepo;

	@Autowired
	private ApplicationRepository applicationRepo;

	@Override
	public ResponseEntity<InterviewScheduleResponseDto> scheduleInterview(InterviewScheduleRequestDto dto)
			throws ApplicationNotFoundException {

		log.info("Starting interview scheduling process for application ID: {}", dto.getApplicationId());
		try {
			ApplicationEntity application = applicationRepo.findById(dto.getApplicationId()).orElseThrow(() -> {
				log.error("Application not found with ID: {}", dto.getApplicationId());
				return new ApplicationNotFoundException("Candidate Application not found");
			});
			InterviewEntity interview = new InterviewEntity();
			interview.setApplication(application);
			interview.setInterviewType(dto.getInterviewType());
			interview.setInterviewStage(dto.getInterviewStage());
			interview.setScheduledDate(dto.getScheduledDate());
			interview.setDurationMinutes(dto.getDurationMinutes());
			interview.setLocation(dto.getLocation());
			interview.setMeetingLink(dto.getMeetingLink());
			interview.setInterviewNotes(dto.getInterviewNotes());
			interview.setInterviewStatus(dto.getInterviewStatus());
			interview.setIsActive(dto.getIsActive());

			InterviewEntity saved = interviewRepo.save(interview);
			log.info("Interview saved successfully with ID: {}", saved.getId());

			InterviewScheduleResponseDto response = mapToResponse(saved);
			log.info("Interview scheduling completed successfully for application ID: {}", dto.getApplicationId());

			return ResponseEntity.ok(response);

		} catch (ApplicationNotFoundException e) {
			log.error("Application not found during interview scheduling for application ID: {}",
					dto.getApplicationId(), e);
			throw e;
		} catch (Exception e) {
			log.error("Unexpected error occurred while scheduling interview for application ID: {}",
					dto.getApplicationId(), e);
			throw e;
		}
	}

	@Override
	public ResponseEntity<InterviewScheduleResponseDto> updateInterview(InterviewScheduleUpdateRequestDto dto)
			throws InterviewNotFoundException {

		log.info("Starting interview update process for interview ID: {}", dto.getInterviewId());
		try {
			InterviewEntity interview = interviewRepo.findById(dto.getInterviewId()).orElseThrow(() -> {
				log.error("Interview not found with ID: {}", dto.getInterviewId());
				return new InterviewNotFoundException("Interview not found with id " + dto.getInterviewId());
			});

			log.info("Interview found successfully for ID: {}", dto.getInterviewId());

			int updatedFieldsCount = 0;

			if (dto.getInterviewType() != null) {
				interview.setInterviewType(dto.getInterviewType());
				updatedFieldsCount++;
			}
			if (dto.getInterviewStage() != null) {
				interview.setInterviewStage(dto.getInterviewStage());
				updatedFieldsCount++;
			}
			if (dto.getScheduledDate() != null) {
				interview.setScheduledDate(dto.getScheduledDate());
				updatedFieldsCount++;
			}
			if (dto.getDurationMinutes() != null) {
				interview.setDurationMinutes(dto.getDurationMinutes());
				updatedFieldsCount++;
			}
			if (dto.getLocation() != null) {
				interview.setLocation(dto.getLocation());
				updatedFieldsCount++;
			}
			if (dto.getMeetingLink() != null) {
				interview.setMeetingLink(dto.getMeetingLink());
				updatedFieldsCount++;
			}
			if (dto.getInterviewNotes() != null) {
				interview.setInterviewNotes(dto.getInterviewNotes());
				updatedFieldsCount++;
			}
			if (dto.getInterviewStatus() != null) {
				interview.setInterviewStatus(dto.getInterviewStatus());
				updatedFieldsCount++;
			}
			if (dto.getIsActive() != null) {
				updatedFieldsCount++;
			}

			log.info("Updated {} fields for interview ID: {}", updatedFieldsCount, dto.getInterviewId());
			InterviewEntity updated = interviewRepo.save(interview);
			log.info("Interview updated successfully with ID: {}", updated.getId());

			InterviewScheduleResponseDto response = mapToResponse(updated);
			log.info("Interview update completed successfully for interview ID: {}", dto.getInterviewId());

			return ResponseEntity.ok(response);

		} catch (InterviewNotFoundException e) {
			log.error("Interview not found during update for interview ID: {}", dto.getInterviewId(), e);
			throw e;
		} catch (Exception e) {
			log.error("Unexpected error occurred while updating interview for ID: {}", dto.getInterviewId(), e);
			throw e;
		}
	}

	private InterviewScheduleResponseDto mapToResponse(InterviewEntity entity) {
		log.debug("Mapping interview entity to response DTO for interview ID: {}", entity.getId());
		InterviewScheduleResponseDto response = new InterviewScheduleResponseDto(entity.getId(),
				entity.getApplication().getId(), entity.getInterviewType(), entity.getInterviewStage(),
				entity.getScheduledDate(), entity.getDurationMinutes(), entity.getLocation(), entity.getMeetingLink(),
				entity.getInterviewNotes(), entity.getInterviewStatus(), entity.getIsActive());
		return response;
	}
}