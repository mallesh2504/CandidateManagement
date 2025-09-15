package com.CandidateManagement.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.CandidateManagement.dto.DocumentResponseDto;
import com.CandidateManagement.entity.DocumentType;

public interface DocumentServiceInterface {
	DocumentResponseDto uploadDocument(MultipartFile file, Long candidateId, DocumentType documentType);

	DocumentResponseDto updateDocument(Long documentId, MultipartFile file);

	byte[] downloadDocument(Long documentId);

	String previewDocument(Long documentId);

	List<DocumentResponseDto> getDocumentsByCandidate(Long candidateId);

	void deleteDocument(Long documentId);
}
