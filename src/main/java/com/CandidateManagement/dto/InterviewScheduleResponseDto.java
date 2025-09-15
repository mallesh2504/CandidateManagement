package com.CandidateManagement.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewScheduleResponseDto {
	private Long interviewId;
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
