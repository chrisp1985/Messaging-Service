package com.chrisp1985.messaging.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestDto {
    Integer customerId;
    Integer templateId;
    DocumentData documentData;
}
