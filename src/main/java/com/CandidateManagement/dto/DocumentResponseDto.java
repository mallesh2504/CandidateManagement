package com.CandidateManagement.dto;

import java.time.LocalDateTime;

import com.CandidateManagement.entity.DocumentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentResponseDto {
    private Long id;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String s3Url;
    private DocumentType documentType;
    private LocalDateTime uploadedAt;
    private Long candidateId;
}
