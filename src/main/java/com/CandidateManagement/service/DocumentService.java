package com.CandidateManagement.service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.CandidateManagement.dto.DocumentResponseDto;
import com.CandidateManagement.entity.CandidateEntity;
import com.CandidateManagement.entity.DocumentEntity;
import com.CandidateManagement.entity.DocumentType;
import com.CandidateManagement.repository.CandidateRepository;
import com.CandidateManagement.repository.DocumentRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DocumentService implements DocumentServiceInterface {

	@Autowired
	private DocumentRepository documentRepo;

	@Autowired
	private CandidateRepository candidateRepo;

	@Autowired
	private S3Service s3Service;

	@Value("${app.aws.bucket.resume}")
	private String resumeBucket;

	@Value("${app.aws.bucket.interview_letter}")
	private String interviewBucket;

	@Value("${app.aws.bucket.offer_letter}")
	private String offerBucket;

	@Value("${app.aws.bucket.timesheet}")
	private String timesheetBucket;

	@Value("${app.aws.bucket.payroll}")
	private String payrollBucket;

	@Value("${app.aws.bucket.general}")
	private String generalBucket;

	private String bucketFor(DocumentType documentType) {
		if (documentType == null)
			return generalBucket;
		switch (documentType) {
		case RESUME:
			return resumeBucket;
		case INTERVIEW_LETTER:
			return interviewBucket;
		case OFFER_LETTER:
			return offerBucket;
		case TIMESHEET:
			return timesheetBucket;
		case PAYROLL:
			return payrollBucket;
		case GENERAL:
		default:
			return generalBucket;
		}
	}

	@Override
	public DocumentResponseDto uploadDocument(MultipartFile file, Long candidateId, DocumentType documentType) {
		log.info("Starting document upload for candidate ID: {}, type: {}, file: {}", candidateId, documentType,
				file.getOriginalFilename());
		try {
			CandidateEntity candidate = candidateRepo.findById(candidateId)
					.orElseThrow(() -> new RuntimeException("Candidate not found with ID: " + candidateId));

			String bucketName = bucketFor(documentType);

			String keyName = candidateId + "-" + (documentType != null ? documentType.name() : "OTHER") + "-"
					+ UUID.randomUUID() + "-" + file.getOriginalFilename();

			String s3Url = s3Service.uploadFileToBucket(file, bucketName, keyName);

			DocumentEntity document = DocumentEntity.builder().fileName(file.getOriginalFilename())
					.fileType(file.getContentType()).fileSize(file.getSize()).bucketName(bucketName).keyName(keyName)
					.s3Url(s3Url).uploadedAt(LocalDateTime.now()).candidate(candidate).documentType(documentType)
					.build();

			DocumentEntity saved = documentRepo.save(document);

			return DocumentResponseDto.builder().id(saved.getId()).fileName(saved.getFileName())
					.fileType(saved.getFileType()).fileSize(saved.getFileSize()).s3Url(saved.getS3Url())
					.uploadedAt(saved.getUploadedAt()).candidateId(candidateId).documentType(saved.getDocumentType())
					.build();

		} catch (IOException e) {
			log.error("IO error during document upload", e);
			throw new RuntimeException("Error uploading file to S3", e);
		}
	}

	@Override
	public DocumentResponseDto updateDocument(Long documentId, MultipartFile file) {
		log.info("Starting document update for document ID: {}, new file: {}", documentId, file.getOriginalFilename());

		try {
			DocumentEntity existingDoc = documentRepo.findById(documentId)
					.orElseThrow(() -> new RuntimeException("Document not found with ID: " + documentId));

			String bucketName = existingDoc.getBucketName() == null ? generalBucket : existingDoc.getBucketName();

			String keyName = existingDoc.getKeyName();
			if (keyName == null || keyName.isBlank()) {
				keyName = existingDoc.getCandidate().getId() + "-" + existingDoc.getDocumentType() + "-"
						+ UUID.randomUUID() + "-" + file.getOriginalFilename();
			}

			String s3Url = s3Service.uploadFileToBucket(file, bucketName, keyName);

			existingDoc.setFileName(file.getOriginalFilename());
			existingDoc.setFileType(file.getContentType());
			existingDoc.setFileSize(file.getSize());
			existingDoc.setS3Url(s3Url);
			existingDoc.setUploadedAt(LocalDateTime.now());
			existingDoc.setBucketName(bucketName);
			existingDoc.setKeyName(keyName);

			DocumentEntity updated = documentRepo.save(existingDoc);

			return DocumentResponseDto.builder().id(updated.getId()).fileName(updated.getFileName())
					.fileType(updated.getFileType()).fileSize(updated.getFileSize()).s3Url(updated.getS3Url())
					.uploadedAt(updated.getUploadedAt()).candidateId(updated.getCandidate().getId())
					.documentType(updated.getDocumentType()).build();

		} catch (IOException e) {
			log.error("IO error during document update", e);
			throw new RuntimeException("Error updating file in S3", e);
		}
	}

	public DocumentEntity getDocumentById(Long documentId) {
		log.info("Fetching document by ID: {}", documentId);
		return documentRepo.findById(documentId)
				.orElseThrow(() -> new RuntimeException("Document not found with ID: " + documentId));
	}

	@Override
	public byte[] downloadDocument(Long documentId) {
		log.info("Starting document download for document ID: {}", documentId);

		DocumentEntity document = documentRepo.findById(documentId)
				.orElseThrow(() -> new RuntimeException("Document not found with ID: " + documentId));

		String bucketName = document.getBucketName() == null ? generalBucket : document.getBucketName();
		String keyName = document.getKeyName();
		byte[] fileData = s3Service.downloadFile(bucketName, keyName);

		log.info("Downloaded document ID: {}, size: {}", documentId, fileData.length);
		return fileData;
	}

	@Override
	public String previewDocument(Long documentId) {
		log.info("Generating preview URL for document ID: {}", documentId);

		DocumentEntity document = documentRepo.findById(documentId)
				.orElseThrow(() -> new RuntimeException("Document not found with ID: " + documentId));

		String bucketName = document.getBucketName() == null ? generalBucket : document.getBucketName();
		String keyName = document.getKeyName();

		String presignedUrl = s3Service.generatePresignedUrl(bucketName, keyName, Duration.ofMinutes(10));
		log.info("Generated presigned URL for document ID: {}", documentId);
		return presignedUrl;
	}

	@Override
	public List<DocumentResponseDto> getDocumentsByCandidate(Long candidateId) {
		List<DocumentEntity> documents = documentRepo.findByCandidate_Id(candidateId);
		return documents.stream()
				.map(doc -> DocumentResponseDto.builder().id(doc.getId()).fileName(doc.getFileName())
						.fileType(doc.getFileType()).fileSize(doc.getFileSize()).s3Url(doc.getS3Url())
						.uploadedAt(doc.getUploadedAt()).candidateId(candidateId).documentType(doc.getDocumentType())
						.build())
				.toList();
	}

	@Override
	public void deleteDocument(Long documentId) {
		log.info("Deleting document ID: {}", documentId);
		DocumentEntity document = documentRepo.findById(documentId)
				.orElseThrow(() -> new RuntimeException("Document not found with ID: " + documentId));

		String bucketName = document.getBucketName() == null ? generalBucket : document.getBucketName();
		String keyName = document.getKeyName();

		s3Service.deleteFile(bucketName, keyName);
		documentRepo.delete(document);

		log.info("Deleted document ID: {} from S3 (bucket: {}, key: {}) and DB", documentId, bucketName, keyName);
	}
}
