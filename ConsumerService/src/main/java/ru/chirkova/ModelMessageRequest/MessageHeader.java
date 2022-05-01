package ru.chirkova.ModelMessageRequest;

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


}