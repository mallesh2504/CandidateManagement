package com.CandidateManagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.CandidateManagement.dto.DocumentResponseDto;
import com.CandidateManagement.entity.DocumentEntity;
import com.CandidateManagement.entity.DocumentType;
import com.CandidateManagement.service.DocumentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/documents")
@Tag(name = "Document Management", description = "APIs for managing candidate documents (resumes, certificates, etc.)")
public class DocumentController {

	@Autowired
	private DocumentService documentService;

	@PostMapping
	@Operation(summary = "Upload document", description = "Upload a new document for a candidate (resume, certificate, etc.)")
	public ResponseEntity<DocumentResponseDto> uploadDocument(@RequestParam("file") MultipartFile file,
			@RequestParam("candidateId") Long candidateId, @RequestParam("documentType") DocumentType documentType) {
		log.info("Received a Request to Upload the Document for a Candidate :{}", candidateId);
		DocumentResponseDto response = documentService.uploadDocument(file, candidateId, documentType);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/update/{documentId}")
	@Operation(summary = "Update document", description = "Replace an existing document with a new file")
	public ResponseEntity<DocumentResponseDto> updateDocument(@PathVariable Long documentId,
			@RequestParam("file") MultipartFile file) {
		log.info("Received a Request to Update the Document  :{}", documentId);
		DocumentResponseDto response = documentService.updateDocument(documentId, file);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/download/{documentId}")
	@Operation(summary = "Download document", description = "Download a document file by its ID")
	public ResponseEntity<byte[]> downloadDocument(@PathVariable Long documentId) {

		log.info("Received a Request to Download the Document  :{}", documentId);

		DocumentEntity doc = documentService.getDocumentById(documentId);
		byte[] fileBytes = documentService.downloadDocument(documentId);

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + doc.getFileName())
				.contentType(MediaType.parseMediaType(doc.getFileType())).body(fileBytes);
	}

	@GetMapping("/preview/{documentId}")
	@Operation(summary = "Preview document", description = "Get a signed URL to preview a document (valid for 10 minutes)")
	public ResponseEntity<String> previewDocument(@PathVariable Long documentId) {
		log.info("Received a Request to Preview the Document  :{}", documentId);
		String signedUrl = documentService.previewDocument(documentId);
		return ResponseEntity.ok(signedUrl);
	}

	@GetMapping("/candidate/{candidateId}")
	@Operation(summary = "Get candidate documents", description = "Retrieve all documents uploaded by a specific candidate")
	public ResponseEntity<List<DocumentResponseDto>> getDocumentsByCandidate(@PathVariable Long candidateId) {
		log.info("Received a Request to get All  Documents that belongs to  :{}", candidateId);
		return ResponseEntity.ok(documentService.getDocumentsByCandidate(candidateId));
	}

	@DeleteMapping("/{documentId}")
	@Operation(summary = "Delete document", description = "Permanently delete a document from storage and database")
	public ResponseEntity<String> deleteDocument(@PathVariable Long documentId) {
		log.info("Received a Request to Delete Document :{}", documentId);
		documentService.deleteDocument(documentId);
		return ResponseEntity.ok("Document deleted successfully " + documentId);
	}
}