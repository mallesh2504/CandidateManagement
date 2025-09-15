package com.CandidateManagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CandidateManagement.entity.InterviewEntity;

public interface InterviewRepository extends JpaRepository<InterviewEntity, Long> {
	List<InterviewEntity> findByApplication_Candidate_Id(Long candidateId);
}
