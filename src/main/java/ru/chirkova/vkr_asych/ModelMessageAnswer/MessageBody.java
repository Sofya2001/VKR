package ru.chirkova.vkr_asych.ModelMessageAnswer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"topic", "partition","offset","error"})
public class MessageBody {
    private String topic;
    private String partition;
    private String offset;
    private ErrorMessage error;

    public MessageBody( String topic, String partition, String offset) {
        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
    }

    public MessageBody(ErrorMessage error) {
        this.error = error;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPartition() {
        return partition;
    }

    public void setPartition(String partition) {
        this.partition = partition;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public ErrorMessage getError() {
        return error;
    }

    public void setError(ErrorMessage error) {
        this.error = error;
    }

    public String toStringWithoutError() {
        return
                "topic='" + topic + '\'' +
                ", partition='" + partition + '\'' +
                ", offset='" + offset + '\''
                ;
    }

    public String toStringWithError() {
        return "{" +
                "error=" + error +
                '}';
    }
}

