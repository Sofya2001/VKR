package ru.chirkova.vkr_asych.ModelMessageRequest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"title", "id","timestamp","service","correlationId"})
public class MessageHeader {
    String title;
    String id;
    String timestamp;
    String service;
    String correlationId;

    public MessageHeader(String title, String id, String timestamp, String service, String correlationId) {
        this.title = title;
        this.id = id;
        this.timestamp = timestamp;
        this.service = service;
        this.correlationId = correlationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    @Override
    public String toString() {
        return "MessageHeader{" +
                "title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", service='" + service + '\'' +
                ", correlationId='" + correlationId + '\'' +
                '}';
    }
}
