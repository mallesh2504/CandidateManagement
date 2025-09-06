package com.CandidateManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CandidateManagement.entity.CandidateEntity;

@Repository
public interface CandidateRepository extends JpaRepository<CandidateEntity, Long> {

	Optional<CandidateEntity> findByEmail(String email);

	List<CandidateEntity> findBySource(String source);

	List<CandidateEntity> findByLocation(String location);
}
