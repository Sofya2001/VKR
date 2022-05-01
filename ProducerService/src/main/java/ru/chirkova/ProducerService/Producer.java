package ru.chirkova.ProducerService;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.chirkova.ModelMessageAnswer.MessageAnswer;
import ru.chirkova.ModelMessageRequest.MessageRequest;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class Producer {
    @Value("${kafka.reply.topic}")
    private String REPLY_TOPICS;
    @Value("${kafka.request.topic}")
    private String SEND_TOPICS;

    @Autowired
    private ReplyingKafkaTemplate<String, MessageRequest, MessageAnswer> template;

    public  Object kafkaRequestReply(MessageRequest request) throws Exception {
        ProducerRecord<String, MessageRequest> record = new ProducerRecord<>(SEND_TOPICS, request);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC,REPLY_TOPICS.getBytes()));
        RequestReplyFuture<String, MessageRequest, MessageAnswer> replyFuture = template.sendAndReceive(record);

        SendResult<String, MessageRequest> sendResult = replyFuture.getSendFuture().get(30, TimeUnit.SECONDS);
        sendResult.getProducerRecord().headers().forEach(header -> System.out.println(header.key()+":"+header.value().toString()));

        ConsumerRecord<String, MessageAnswer> consumerRecord = replyFuture.get(30, TimeUnit.SECONDS);

        return  consumerRecord.value();
}
}
