package com.chrisp1985.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentChangeEvent {
    private String operationType;
    private String objectId;
    private String status;
    private int customerId;
    private int templateId;
    private Map<String, Object> documentData;
}

