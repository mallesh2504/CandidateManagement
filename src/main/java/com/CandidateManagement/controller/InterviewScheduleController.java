package com.CandidateManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CandidateManagement.dto.InterviewScheduleRequestDto;
import com.CandidateManagement.dto.InterviewScheduleResponseDto;
import com.CandidateManagement.dto.InterviewScheduleUpdateRequestDto;
import com.CandidateManagement.exception.ApplicationNotFoundException;
import com.CandidateManagement.exception.InterviewNotFoundException;
import com.CandidateManagement.service.InterviewService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/interviews")
public class InterviewScheduleController {

	@Autowired
	private InterviewService interviewService;

	@PostMapping
	@Operation(summary = "Schedule a new interview", description = "Creates a new interview schedule for a candidate application. ")
	public ResponseEntity<InterviewScheduleResponseDto> scheduleInterview(@RequestBody InterviewScheduleRequestDto dto)
			throws ApplicationNotFoundException {
		log.info("Received request to schedule interview for application ID: {}", dto.getApplicationId());
		return interviewService.scheduleInterview(dto);
	}

	@PutMapping
	@Operation(summary = "Update an existing interview schedule", description = "Updates the details of an existing interview schedule.")
	public ResponseEntity<InterviewScheduleResponseDto> updateScheduleInterview(
			@RequestBody InterviewScheduleUpdateRequestDto dto) throws InterviewNotFoundException {

		log.info("Received request to update interview schedule for interview ID: {}", dto.getInterviewId());
		return interviewService.updateInterview(dto);
	}
}
