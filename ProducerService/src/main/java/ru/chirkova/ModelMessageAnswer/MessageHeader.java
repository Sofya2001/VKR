package ru.chirkova.ModelMessageAnswer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageHeader {
    private String title;
    private String id;
    private String timestamp;
    private String service;
    private String status;
}