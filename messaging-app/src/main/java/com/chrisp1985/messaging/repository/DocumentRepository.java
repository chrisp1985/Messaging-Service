package com.chrisp1985.messaging.repository;

import com.chrisp1985.messaging.model.Request;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends MongoRepository<Request, String> {
}
