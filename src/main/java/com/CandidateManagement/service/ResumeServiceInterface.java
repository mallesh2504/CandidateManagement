package com.CandidateManagement.service;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.CandidateManagement.dto.ResumeResponseDto;
import com.CandidateManagement.entity.ResumeEntity;
import com.CandidateManagement.exception.CandidateNotFoundException;
import com.CandidateManagement.exception.ResumeIdNotFoundException;
import com.CandidateManagement.exception.ResumeandCandidateNotMatched;

public interface ResumeServiceInterface {

	ResumeEntity uploadResumeFile(MultipartFile file, Long candidateId) throws CandidateNotFoundException, IOException;

	ResumeEntity updateResumeFile(Long id, MultipartFile file, Long candidateId)
			throws ResumeIdNotFoundException, ResumeandCandidateNotMatched, IOException;

	ResponseEntity<ResumeResponseDto> getResume(Long id, Long candidateId)
			throws ResumeIdNotFoundException, ResumeandCandidateNotMatched;

	ResumeEntity downloadResume(Long id) throws ResumeIdNotFoundException;

	String deleteResume(Long id, Long candidateId) throws ResumeIdNotFoundException, ResumeandCandidateNotMatched;
}
