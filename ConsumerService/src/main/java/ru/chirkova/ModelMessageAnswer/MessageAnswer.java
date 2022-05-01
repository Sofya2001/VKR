package ru.chirkova.ModelMessageAnswer;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageAnswer {
    MessageHeader header;
    MessageBody body;
}