package com.chrisp1985.services;

import com.chrisp1985.model.DocumentChangeEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Slf4j
@Service
public class MessageQueueListener {

    DocumentConversionService documentConversionService;
    BlobStorageService blobStorageService;
    ObjectMapper objectMapper;

    public MessageQueueListener(DocumentConversionService documentConversionService,
                                BlobStorageService blobStorageService,
                                ObjectMapper objectMapper) {
        this.documentConversionService = documentConversionService;
        this.blobStorageService = blobStorageService;
        this.objectMapper = objectMapper;
    }

    @SqsListener(value = "${spring.cloud.aws.sqs.documentdb.name}")
    public void handleDocDbEvent(String messageJson) {
        String s3Key = "";
        try {
            DocumentChangeEvent event = objectMapper.readValue(messageJson, DocumentChangeEvent.class);

            Map<String, Object> documentData = event.getDocumentData();
            String objectId = event.getObjectId();
            Integer customerId = event.getCustomerId();

            log.info("Extracted documentData: {}", documentData);

            s3Key = customerId + "/" + objectId;

            File convertedFile = documentConversionService.processTemplateFromS3(s3Key);
            blobStorageService.addDocument(String.valueOf(customerId), convertedFile);

        } catch (Exception e) {
            log.error("Failed to parse incoming message with key: {}", s3Key, e);
        }
    }

}
