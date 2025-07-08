package com.chrisp1985.messaging.controller;

import com.chrisp1985.messaging.model.RequestDto;
import com.chrisp1985.messaging.service.BlobStorageService;
import com.chrisp1985.messaging.service.DataStoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/template")
public class TemplateController {

    DataStoreService dataStoreService;
    BlobStorageService blobStorageService;
    ObjectMapper objectMapper;

    @Autowired
    public TemplateController(DataStoreService dataStoreService, BlobStorageService blobStorageService, ObjectMapper objectMapper) {
        this.dataStoreService = dataStoreService;
        this.blobStorageService = blobStorageService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/add")
    public ResponseEntity<String> postTemplate(@RequestBody RequestDto requestDto) {
        String id = this.dataStoreService.storeRequest(requestDto);
        return ResponseEntity.ok(id);
    }

    @PostMapping(value = "/generate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> generateDocument(
            @RequestPart("template") MultipartFile templateFile,
            @RequestPart("data") String dataJson
    ) throws IOException {

        RequestDto requestDto = objectMapper.readValue(dataJson, RequestDto.class);
        blobStorageService.addDocument(requestDto.getCustomerId(), requestDto.getTemplateId(), templateFile);

        String id = this.dataStoreService.storeRequest(requestDto);
        return ResponseEntity.ok(id);
    }
}
