package com.chrisp1985.messaging.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ChangeStreamListener {

    private MongoClient mongoClient;
    private QueueRequestService queueRequestService;

    public ChangeStreamListener(MongoClient mongoClient, QueueRequestService queueRequestService) {
        this.mongoClient = mongoClient;
        this.queueRequestService = queueRequestService;
    }


    @PostConstruct
    public void initChangeStream() {
        new Thread(() -> {
            MongoDatabase database = mongoClient.getDatabase("testdb");
            MongoCollection<Document> collection = database.getCollection("requests");
            Bson matchInsert = Aggregates.match(Filters.eq("operationType", "insert"));

            collection.watch(List.of(matchInsert))
                    .fullDocument(FullDocument.UPDATE_LOOKUP)
                    .forEach((ChangeStreamDocument<Document> doc) -> {
                        log.info("Insert detected: {}", doc);
                        queueRequestService.sendToQueue(doc);
                    });
        }).start();
    }
}
