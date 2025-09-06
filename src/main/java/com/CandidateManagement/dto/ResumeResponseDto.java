package com.CandidateManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumeResponseDto {
    private Long id;
    private String fileName;
    private String fileType;
}
