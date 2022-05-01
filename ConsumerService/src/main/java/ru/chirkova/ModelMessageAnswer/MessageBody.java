package ru.chirkova.ModelMessageAnswer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageBody {
    private String correlationID;
    private String topic;
    private String partition;
    private String offset;
}