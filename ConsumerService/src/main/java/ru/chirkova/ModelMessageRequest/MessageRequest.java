package ru.chirkova.ModelMessageRequest;


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


