package com.CandidateManagement.dto;

import java.math.BigDecimal;

import com.CandidateManagement.entity.ApplicationStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRequestDto {

    @NotNull(message = "Candidate ID is required")
    private Long candidateId;

    @NotNull(message = "Job ID is required")
    private Long jobId;

    @NotNull(message = "Status is required")
    private ApplicationStatus status;

    private String applicationSource;
    private String applicationNotes;
    private BigDecimal expectedSalary;
    private Integer applicationRating;
    private BigDecimal skillMatchScore;
}