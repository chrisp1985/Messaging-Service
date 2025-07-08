package com.chrisp1985.messaging.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class RequestDto {
    Integer customerId;
    String templateLink;
    Map<String, Object> documentData;
}
