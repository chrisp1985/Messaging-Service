package com.chrisp1985.messaging.service;

// TODO: ALL OF THIS

import com.chrisp1985.messaging.model.sqs.S3EventNotification;
import com.chrisp1985.messaging.repository.DocumentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FileCreationListener {

    DataStoreService dataStoreService;
    private DocumentRepository repository;
    private ObjectMapper objectMapper;

    public FileCreationListener(DataStoreService dataStoreService,
                                DocumentRepository repository,
                                ObjectMapper objectMapper) {
        this.dataStoreService = dataStoreService;
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @SqsListener(value = "${spring.cloud.aws.sqs.s3.name}")
    public void handleS3Event(String messageJson) {

        log.info("Item queued! {}", messageJson);
        String key = extractObjectKey(messageJson);
        updateDocumentDb(key);
    }

    private void updateDocumentDb(String key) {

        log.info("Key Found: {}", key);
        dataStoreService.updateRequest(
                key.split("/")[1]
                .replaceFirst("\\.pdf$", ""));

    }

    public String extractObjectKey(String sqsMessage) {
        S3EventNotification notification = null;
        try {
            notification = objectMapper.readValue(sqsMessage, S3EventNotification.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (notification.getRecords() != null && !notification.getRecords().isEmpty()) {
            return notification.getRecords().get(0).getS3().getObject().getKey();
        }

        throw new IllegalArgumentException("No S3 object key found in message");
    }
}
