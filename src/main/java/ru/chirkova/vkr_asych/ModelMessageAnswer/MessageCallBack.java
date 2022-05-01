package ru.chirkova.vkr_asych.ModelMessageAnswer;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"header","body"})
public class MessageCallBack {
    MessageHeader header;
    MessageBody body;

    public MessageCallBack(MessageHeader header, MessageBody body) {
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


    public String toStringWithoutError() {
        return "{" +
                "header=" + header +
                ", body=" + body.toStringWithoutError() +
                '}';
    }

    public String toStringWithError() {
        return "{" +
                "header=" + header +
                ", body=" + body.toStringWithError() +
                '}';
    }
}
