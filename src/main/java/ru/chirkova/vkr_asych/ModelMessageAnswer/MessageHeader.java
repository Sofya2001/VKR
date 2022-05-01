package ru.chirkova.vkr_asych.ModelMessageAnswer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"title", "id","timestamp","service","status"})
public class MessageHeader {
    private String title;
    private String id;
    private String timestamp;
    private String service;
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MessageHeader(String title, String id, String timestamp, String service,  String status) {
        this.title = title;
        this.id = id;
        this.timestamp = timestamp;
        this.service = service;
        this.status = status;
    }

    @Override
    public String toString() {
        return "{" +
                "title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", service='" + service + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}