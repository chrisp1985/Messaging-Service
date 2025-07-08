package com.chrisp1985.messaging.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("my_documents")
@Data
public class DocumentData {

    String name;
    String address;
}
