package com.chrisp1985.messaging.model.sqs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentChangeEventDTO {
    private String operationType;
    private String objectId;
    private String status;
    private int customerId;
    private int templateId;
    private Map<String, Object> documentData;
}

