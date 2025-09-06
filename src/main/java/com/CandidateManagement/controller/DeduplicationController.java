package com.CandidateManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CandidateManagement.dto.DeduplicationResultDto;
import com.CandidateManagement.exception.CandidateNotFoundException;
import com.CandidateManagement.service.DeduplicationService;

@RestController
@RequestMapping("/Deduplication")
public class DeduplicationController {

	@Autowired
	private DeduplicationService deduplicationService;
	
	@GetMapping("/check/{candidateId}")
	public ResponseEntity<DeduplicationResultDto> checkDuplicate(@PathVariable Long candidateId) throws CandidateNotFoundException{
		return deduplicationService.checkDuplicate(candidateId);
	}
}
