package com.CandidateManagement.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateDashboardDto {
	private CandidateResponseDto candidateProfile;
	private List<ApplicationDto> applications;
	private List<DocumentDto> documents;
	private List<InterviewDto> interviews;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ApplicationDto {
		private Long applicationId;
		private Long jobId;
		private String applicationStatus;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class DocumentDto {
		private Long documentId;
		private String fileName;
		private String fileType;
		private String s3Url;
		private String documentType;
		private String uploadedAt;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class InterviewDto {
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
}
