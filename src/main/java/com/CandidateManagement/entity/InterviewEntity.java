package com.CandidateManagement.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "interviews")
public class InterviewEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "job_application_id", nullable = false) 
	private ApplicationEntity application;

	@Column(name = "interview_type", length = 50)
	private String interviewType; 

	@Column(name = "interview_stage", length = 100)
	private String interviewStage; 

	@Column(name = "scheduled_date", nullable = false)
	private LocalDateTime scheduledDate; 

	@Column(name = "duration_minutes")
	private Integer durationMinutes;

	@Column(length = 255)
	private String location;

	@Column(name = "meeting_link", length = 500)
	private String meetingLink;

	@Column(name = "interview_notes", columnDefinition = "TEXT")
	private String interviewNotes;

	@Column(name = "interview_status", length = 50)
	private String interviewStatus;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = "is_active")
	private Boolean isActive = true;
}
