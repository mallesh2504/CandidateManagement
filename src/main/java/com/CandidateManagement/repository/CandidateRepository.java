package com.CandidateManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.CandidateManagement.entity.CandidateEntity;

public interface CandidateRepository extends JpaRepository<CandidateEntity, Long> {

	List<CandidateEntity> findByEmailOrPhone(String email, String phone);

	Optional<CandidateEntity> findByEmail(String email);

	List<CandidateEntity> findBySource(String source);

	List<CandidateEntity> findByLocation(String location);

	// Search candidates by skill keyword
	@Query("SELECT c FROM CandidateEntity c WHERE LOWER(c.skills) LIKE %:skill%")
	List<CandidateEntity> findBySkill(@Param("skill") String skill);
}
