package com.CandidateManagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CandidateManagement.entity.DocumentEntity;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
	List<DocumentEntity> findByCandidate_Id(Long candidateId); // ✅ Added
}
