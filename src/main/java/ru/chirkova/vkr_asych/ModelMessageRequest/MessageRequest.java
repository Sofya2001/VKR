package ru.chirkova.vkr_asych.ModelMessageRequest;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"header","body"})
public class MessageRequest {
    MessageHeader header;
    MessageBody body;

    public MessageRequest(MessageHeader header, MessageBody body) {
        this.header = header;
        this.body = body;
    }

    public MessageHeader getHeader() {
        return header;
    }

    public void setHeader(MessageHeader header) {
        this.header = header;
    }

    public MessageBody getBody() {
        return body;
    }

    public void setBody(MessageBody body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "MessageSender{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }
}

