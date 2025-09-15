package com.CandidateManagement.service;

import java.io.IOException;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class S3Service {

	private final S3Client s3Client;

	@Value("${app.aws.region}")
	private String region;

	public String uploadFileToBucket(MultipartFile file, String bucketName, String keyName) throws IOException {
		PutObjectRequest request = PutObjectRequest.builder().bucket(bucketName).key(keyName)
				.contentType(file.getContentType()).build();

		s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
		return buildS3Url(bucketName, keyName);
	}

	public byte[] downloadFile(String bucketName, String keyName) {
		GetObjectRequest request = GetObjectRequest.builder().bucket(bucketName).key(keyName).build();

		ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(request);
		return objectBytes.asByteArray();
	}

	public void deleteFile(String bucketName, String keyName) {
		DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(bucketName).key(keyName).build();
		s3Client.deleteObject(request);
	}

	public String generatePresignedUrl(String bucketName, String keyName, Duration expiry) {
		S3Presigner presigner = S3Presigner.builder().region(Region.of(region)).build();

		GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(keyName).build();

		GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder().signatureDuration(expiry)
				.getObjectRequest(getObjectRequest).build();

		String url = presigner.presignGetObject(presignRequest).url().toString();
		presigner.close();
		return url;
	}

	private String buildS3Url(String bucketName, String keyName) {
		return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + keyName;
	}
}
