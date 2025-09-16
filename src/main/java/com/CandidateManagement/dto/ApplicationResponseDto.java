package com.CandidateManagement.dto;

import com.CandidateManagement.entity.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponseDto {
    private Long id;
    private Long candidateId;
    private Long jobId;
    private ApplicationStatus status;
    private String applicationStage;
    private String applicationSource;
    private String applicationNotes;
    private BigDecimal expectedSalary;
    private Integer applicationRating;
    private BigDecimal skillMatchScore;
    private LocalDateTime createdAt;
    private Boolean isActive;
}