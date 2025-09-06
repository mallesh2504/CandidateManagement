package com.CandidateManagement.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.CandidateManagement.dto.ResumeResponseDto;
import com.CandidateManagement.entity.CandidateEntity;
import com.CandidateManagement.entity.ResumeEntity;
import com.CandidateManagement.exception.CandidateNotFoundException;
import com.CandidateManagement.exception.ResumeIdNotFoundException;
import com.CandidateManagement.exception.ResumeandCandidateNotMatched;
import com.CandidateManagement.repository.CandidateRepository;
import com.CandidateManagement.repository.ResumeRepository;

@Service
public class ResumeService implements ResumeServiceInterface{

	@Autowired
	private ResumeRepository resumeRepo;

	@Autowired
	private CandidateRepository candidateRepo;

	public ResumeEntity uploadResumeFile(MultipartFile file, Long candidateId)
			throws CandidateNotFoundException, IOException {

		CandidateEntity candidate = candidateRepo.findById(candidateId)
				.orElseThrow(() -> new CandidateNotFoundException(
						"Candidate Not Found With Given Details So Please register first."));

		ResumeEntity resume = new ResumeEntity();

		resume.setFileName(file.getOriginalFilename());
		resume.setFileType(file.getContentType());
		resume.setFileData(file.getBytes());

		resume.setCandidate(candidate);

		candidate.setResume(resume);

		return resumeRepo.save(resume);
	}

	public ResumeEntity updateResumeFile(Long id, MultipartFile file, Long candidateId)
			throws ResumeIdNotFoundException, ResumeandCandidateNotMatched, IOException {

		ResumeEntity existingResume = resumeRepo.findById(id)
				.orElseThrow(() -> new ResumeIdNotFoundException("Resume not found with id: " + id));

		if (!existingResume.getCandidate().getId().equals(candidateId)) {
			throw new ResumeandCandidateNotMatched("Resume does not belong to candidateId " + candidateId);
		}

		existingResume.setFileName(file.getOriginalFilename());
		existingResume.setFileType(file.getContentType());
		existingResume.setFileData(file.getBytes());

		return resumeRepo.save(existingResume);
	}

	public ResponseEntity<ResumeResponseDto> getResume(Long id, Long candidateId)
			throws ResumeIdNotFoundException, ResumeandCandidateNotMatched {

		ResumeEntity existingResume = resumeRepo.findById(id)
				.orElseThrow(() -> new ResumeIdNotFoundException("Resume not found. Please upload instead."));

		if (!existingResume.getCandidate().getId().equals(candidateId)) {
			throw new ResumeandCandidateNotMatched("Resume does not belong to candidateId " + candidateId);
		}

		ResumeResponseDto dto = new ResumeResponseDto(existingResume.getId(), existingResume.getFileName(),
				existingResume.getFileType());

		return ResponseEntity.ok(dto);
	}

	public ResumeEntity downloadResume(Long id) throws ResumeIdNotFoundException {
		return resumeRepo.findById(id)
				.orElseThrow(() -> new ResumeIdNotFoundException("Resume not found with id: " + id));
	}

	public String deleteResume(Long id, Long candidateId)
			throws ResumeIdNotFoundException, ResumeandCandidateNotMatched {

		ResumeEntity existingResume = resumeRepo.findById(id)
				.orElseThrow(() -> new ResumeIdNotFoundException("Resume not found."));

		if (!existingResume.getCandidate().getId().equals(candidateId)) {
			throw new ResumeandCandidateNotMatched("Resume does not belong to candidateId " + candidateId);
		}

		CandidateEntity candidate = existingResume.getCandidate();
		candidate.setResume(null);
		candidateRepo.save(candidate);

		
		resumeRepo.deleteById(id);

		return "Resume with id " + id + " deleted successfully for candidate " + candidateId;
	}


}
