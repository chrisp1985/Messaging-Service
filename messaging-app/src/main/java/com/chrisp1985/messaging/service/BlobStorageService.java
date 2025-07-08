package com.chrisp1985.messaging.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.*;

@Slf4j
@Service
public class BlobStorageService {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.input.name}")
    private String bucketName;

    public BlobStorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void addDocument(Integer customerId, Integer templateId, MultipartFile file) {
        String key = customerId + "/" + templateId + ".docx";

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(putRequest, RequestBody.fromInputStream(inputStream, file.getSize()));
            log.info("Document added to S3 at {}!", key);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload document to S3", e);
        }
    }
}

