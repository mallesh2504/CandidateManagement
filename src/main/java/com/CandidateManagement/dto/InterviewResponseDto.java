package com.CandidateManagement.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewResponseDto {
    private Long applicationId;
    private String interviewDetails; 
}
