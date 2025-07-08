package com.chrisp1985.messaging.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "requests")
public class Request {

    @Id
    private String id;

    private Status status = Status.PENDING;
    private Integer customerId;
    private Integer templateId;
    private Map<String, Object> documentData;

}
