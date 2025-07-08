package com.chrisp1985.messaging.service;

import com.chrisp1985.messaging.model.sqs.DocumentChangeEventDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class QueueRequestService {

    private final SqsTemplate sqsTemplate;
    private ObjectMapper objectMapper;

    @Value("${spring.cloud.aws.sqs.documentdb.name}")
    private String sqsDocDbQueueName;

    @Autowired
    public QueueRequestService(SqsTemplate sqsTemplate, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.sqsTemplate = sqsTemplate;
    }

    public void sendToQueue(ChangeStreamDocument<Document> document) {
        try {
            Document fullDoc = document.getFullDocument();
            if (fullDoc == null) {
                log.warn("Skipping change stream event with null fullDocument");
                return;
            }

            String objectId = fullDoc.getObjectId("_id").toHexString();

            DocumentChangeEventDTO dto = new DocumentChangeEventDTO(
                    document.getOperationType().getValue(),
                    objectId,
                    fullDoc.getString("status"),
                    fullDoc.getInteger("customerId"),
                    fullDoc.getInteger("templateId"),
                    fullDoc.get("documentData", Map.class)
            );

            String json = objectMapper.writeValueAsString(dto);
            log.info("Sending DocumentChangeEventDTO to SQS: {}", json);
            sqsTemplate.send(sqsDocDbQueueName, json);

        } catch (Exception e) {
            log.error("Failed to process ChangeStreamDocument", e);
        }
    }
}
