package com.CandidateManagement.service;

import org.springframework.http.ResponseEntity;

import com.CandidateManagement.dto.InterviewScheduleRequestDto;
import com.CandidateManagement.dto.InterviewScheduleResponseDto;
import com.CandidateManagement.dto.InterviewScheduleUpdateRequestDto;
import com.CandidateManagement.exception.ApplicationNotFoundException;
import com.CandidateManagement.exception.InterviewNotFoundException;

public interface InterviewServiceInterface {

	ResponseEntity<InterviewScheduleResponseDto> scheduleInterview(InterviewScheduleRequestDto dto)
			throws ApplicationNotFoundException;

	ResponseEntity<InterviewScheduleResponseDto> updateInterview(InterviewScheduleUpdateRequestDto dto)
			throws InterviewNotFoundException;
}
