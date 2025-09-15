package com.CandidateManagement.dto;

import com.CandidateManagement.entity.DocumentType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentRequestDto {


	@NotNull(message = "Candidate Id is required")
	@NotBlank(message = "Candidate Id cannot be blank")
    private Long candidateId;    
	
	@NotNull(message = "Document Type is required")
	@NotBlank(message = "Document  Type cannot be blank")
    private DocumentType documentType;
	
	@NotNull(message = "File is required")
	@NotBlank(message = "File cannot be blank")
    private MultipartFile file;           
}
