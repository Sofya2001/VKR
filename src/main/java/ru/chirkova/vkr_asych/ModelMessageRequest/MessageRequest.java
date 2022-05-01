package ru.chirkova.vkr_asych.ModelMessageRequest;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class MessageRequest {
        private MessageHeader header;
        private MessageBody body;

    }


