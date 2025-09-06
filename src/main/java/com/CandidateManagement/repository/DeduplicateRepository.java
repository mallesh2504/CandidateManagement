package com.CandidateManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CandidateManagement.entity.DeduplicationEntity;

public interface DeduplicateRepository extends JpaRepository<DeduplicationEntity, Long>{

}
