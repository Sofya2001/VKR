package ru.chirkova.ConsumerService;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import ru.chirkova.ModelMessageAnswer.MessageAnswer;
import ru.chirkova.ModelMessageAnswer.MessageBody;
import ru.chirkova.ModelMessageAnswer.MessageHeader;
import ru.chirkova.ModelMessageRequest.MessageRequest;

import java.time.LocalDateTime;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Slf4j
@Component
public class Consumer {
    @KafkaListener(topics = "${kafka.request.topic}")
    @SendTo
    public MessageAnswer listen(MessageRequest request,
                                @Header(KafkaHeaders.CORRELATION_ID) String corId,
                                @Header(KafkaHeaders.RECEIVED_TOPIC) String recTopic,
                                @Header(KafkaHeaders.RECEIVED_PARTITION_ID) String recPartition,
                                @Header(KafkaHeaders.OFFSET) String recOffset) throws InterruptedException {
        log.info("My log" + String.valueOf(request));
        MessageAnswer answer=new MessageAnswer(
                new MessageHeader(request.getHeader().getTitle(),
                        request.getHeader().getId(),
                        String.valueOf(LocalDateTime.now()),
                        request.getHeader().getService(),
                        "Succesfully"),
                new MessageBody(corId,recTopic,recPartition,recOffset));
        return answer;

    }
}