package com.CandidateManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CandidateManagement.entity.ResumeEntity;

public interface ResumeRepository extends JpaRepository<ResumeEntity, Long>{

}
