package com.CandidateManagement.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.CandidateManagement.entity.CandidateProfileEntity;

public interface CandidateProfileRepository extends JpaRepository<CandidateProfileEntity, Long> {
    Optional<CandidateProfileEntity> findByCandidate_Id(Long candidateId);
}