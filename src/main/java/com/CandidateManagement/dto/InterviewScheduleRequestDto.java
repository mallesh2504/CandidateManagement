package com.CandidateManagement.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewScheduleRequestDto {

	@NotNull(message = "Application Id is required")
	@NotBlank(message = "Application Id cannot be blank")
	private Long applicationId;
	
	private String interviewType;
	private String interviewStage;
	private LocalDateTime scheduledDate;
	private Integer durationMinutes;
	private String location;
	private String meetingLink;
	private String interviewNotes;
	private String interviewStatus;
	private Boolean isActive;
}
