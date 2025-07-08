package com.chrisp1985.messaging.controller;

import com.chrisp1985.messaging.model.RequestDto;
import com.chrisp1985.messaging.service.DataStoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/template")
public class TemplateController {

    DataStoreService dataStoreService;
    ObjectMapper objectMapper;

    @Autowired
    public TemplateController(DataStoreService dataStoreService, ObjectMapper objectMapper) {
        this.dataStoreService = dataStoreService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/add")
    public ResponseEntity<String> postTemplate(@RequestBody RequestDto requestDto) {
        String id = this.dataStoreService.storeRequest(requestDto);
        return ResponseEntity.ok(id);
    }
}
