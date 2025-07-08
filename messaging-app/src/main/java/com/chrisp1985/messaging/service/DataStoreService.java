package com.chrisp1985.messaging.service;

import com.chrisp1985.messaging.model.Request;
import com.chrisp1985.messaging.model.RequestDto;
import com.chrisp1985.messaging.model.RequestMapper;
import com.chrisp1985.messaging.model.Status;
import com.chrisp1985.messaging.repository.DocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class DataStoreService {

    private final DocumentRepository documentRepository;
    private final RequestMapper requestMapper;

    @Autowired
    public DataStoreService(DocumentRepository documentRepository, RequestMapper requestMapper) {
        this.documentRepository = documentRepository;
        this.requestMapper = requestMapper;
    }

    public String storeRequest(RequestDto data) {
        Request request = documentRepository.insert(requestMapper.toRequest(data));
        log.info("Stored request successfully: {}.", data.getDocumentData());
        return request.getId();
    }

    public void updateRequest(String id) {
        Optional<Request> dataObject = this.documentRepository.findById(id);
        if(dataObject.isPresent()) {
            Request dataFound = dataObject.get();
            dataFound.setStatus(Status.COMPLETE);
            documentRepository.save(dataFound);
        }
        log.info("Updated id {} successfully.", id);
    }

    public void getRequest(String id) {
        documentRepository.findById(id);
    }
}
