package com.chrisp1985.messaging.model.sqs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class S3EventNotification {

    @JsonProperty("Records")
    private List<Record> records;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Record {
        private S3 s3;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class S3 {
        private Bucket bucket;
        private ObjectData object;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Bucket {
        private String name;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ObjectData {
        private String key;
        private Long size;
        private String eTag;
    }
}

