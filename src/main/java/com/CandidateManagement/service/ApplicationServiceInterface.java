package com.CandidateManagement.service;

import org.springframework.http.ResponseEntity;

import com.CandidateManagement.dto.ApplicationRequestDto;
import com.CandidateManagement.dto.ApplicationResponseDto;
import com.CandidateManagement.dto.InterviewResponseDto;
import com.CandidateManagement.dto.OfferLetterRequestDto;
import com.CandidateManagement.dto.OfferLetterResponseDto;
import com.CandidateManagement.entity.ApplicationStatus;
import com.CandidateManagement.exception.AlreadyAppliedException;
import com.CandidateManagement.exception.ApplicationNotFoundException;
import com.CandidateManagement.exception.CandidateNotFoundException;

public interface ApplicationServiceInterface {

    ResponseEntity<ApplicationResponseDto> applyCandidateForJob(ApplicationRequestDto request)
            throws CandidateNotFoundException, AlreadyAppliedException;

    ResponseEntity<OfferLetterResponseDto> updateofferLetterUrl(OfferLetterRequestDto request)
            throws ApplicationNotFoundException;

    ResponseEntity<ApplicationResponseDto> getApplicationStatus(Long applicationId) throws ApplicationNotFoundException;

    ResponseEntity<InterviewResponseDto> getInterviewInvites(Long applicationId) throws ApplicationNotFoundException;

    ResponseEntity<OfferLetterResponseDto> downloadOfferLetter(Long applicationId) throws ApplicationNotFoundException;

    ResponseEntity<ApplicationResponseDto> updateApplicationStatus(Long applicationId, ApplicationStatus status, String interviewDetails)
            throws ApplicationNotFoundException;
}
