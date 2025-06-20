package com.chrisp1985.messaging.service;

import com.mongodb.client.model.changestream.ChangeStreamDocument;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class QueueRequestService {

    private final SqsTemplate sqsTemplate;

    @Value("${spring.cloud.aws.sqs.documentdb.name}")
    private String sqsDocDbQueueName;

    @Autowired
    public QueueRequestService(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    public void sendToQueue(ChangeStreamDocument<Document> document) {
        log.info("Sending ID {} to SQS Queue: {}.", document.getFullDocument().getObjectId("_id"), document);
        sqsTemplate.send(sqsDocDbQueueName, document.toString());
    }
}
