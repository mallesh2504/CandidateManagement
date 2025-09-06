package com.CandidateManagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.CandidateManagement.dto.DeduplicationResultDto;
import com.CandidateManagement.entity.CandidateEntity;
import com.CandidateManagement.entity.DeduplicationEntity;
import com.CandidateManagement.exception.CandidateNotFoundException;
import com.CandidateManagement.repository.CandidateRepository;
import com.CandidateManagement.repository.DeduplicateRepository;

@Service
public class DeduplicationService implements DeduplicationServiceInterface {

	@Autowired
	private DeduplicateRepository deduplicateRepo;

	@Autowired
	private CandidateRepository candidateRepo;

	public ResponseEntity<DeduplicationResultDto> checkDuplicate(Long candidateId) throws CandidateNotFoundException {

		Optional<CandidateEntity> existingCandidate = candidateRepo.findById(candidateId);
		if (existingCandidate.isEmpty()) {
			throw new CandidateNotFoundException("Given Candidate Details Are Not Found So Please Register Instead");
		}

		List<CandidateEntity> allCandidates = candidateRepo.findAll();
		CandidateEntity candidate = existingCandidate.get();

		double maxSimilarity = 0.0;
		boolean duplicateFound = false;

		for (CandidateEntity existing : allCandidates) {
			if (!existing.getId().equals(candidate.getId())) {
				double similarity = calculateSimilarity(candidate, existing);
				if (similarity > 0.6) {
					duplicateFound = true;
					maxSimilarity = Math.max(maxSimilarity, similarity);
				}
			}
		}

		DeduplicationEntity entity = new DeduplicationEntity();
		entity.setCandidateId(candidate.getId());
		entity.setDuplicateFound(duplicateFound);
		entity.setSimilarityScore(maxSimilarity);

		deduplicateRepo.save(entity);
		DeduplicationResultDto responseDto = new DeduplicationResultDto();
		responseDto.setCandidateId(entity.getCandidateId());
		responseDto.setDuplicateFound(duplicateFound);
		responseDto.setSimilarityScore(maxSimilarity);

		return new ResponseEntity<DeduplicationResultDto>(responseDto, HttpStatus.FOUND);
	}

	private double calculateSimilarity(CandidateEntity candidate, CandidateEntity existing) {
		int matches = 0;
		if (candidate.getFirstName().equalsIgnoreCase(existing.getFirstName())) {
			matches++;
		}
		if (candidate.getLastName().equalsIgnoreCase(existing.getLastName())) {
			matches++;
		}
		if (candidate.getPhone().equalsIgnoreCase(existing.getPhone())) {
			matches++;
		}
		if (candidate.getSkills().equalsIgnoreCase(existing.getSkills())) {
			matches++;
		}
		return matches / 4.0;
	}

}
