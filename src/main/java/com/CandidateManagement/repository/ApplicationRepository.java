package com.CandidateManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CandidateManagement.entity.ApplicationEntity;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {

    Optional<ApplicationEntity> findByCandidate_IdAndJobId(Long candidateId, Long jobId);

    List<ApplicationEntity> findByCandidate_Id(Long candidateId);

}
