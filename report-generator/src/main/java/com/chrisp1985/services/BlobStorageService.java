package com.chrisp1985.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;

@Slf4j
@Service
public class BlobStorageService {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.generated.name}")
    private String bucketName;

    public BlobStorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void addDocument(String objectId, File file) {

        String key = objectId + "/" + file.getName();

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.putObject(putRequest, RequestBody.fromFile(file));

        log.info("Document added to S3 at {}/{}!", objectId, file.getName());
    }

    public void addDocument(String objectId, String filename, byte[] fileBytes) {
        String key = objectId + "/" + filename;

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("application/pdf")
                .build();

        s3Client.putObject(putRequest, RequestBody.fromBytes(fileBytes));

        log.info("PDF added to S3 at {}/{}!", objectId, filename);
    }
}
