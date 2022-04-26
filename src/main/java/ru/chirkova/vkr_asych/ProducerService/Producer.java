package ru.chirkova.vkr_asych.ProducerService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.chirkova.vkr_asych.ModelMessageAnswer.ErrorMessage;
import ru.chirkova.vkr_asych.ModelMessageAnswer.MessageBody;
import ru.chirkova.vkr_asych.ModelMessageAnswer.MessageCallBack;
import ru.chirkova.vkr_asych.ModelMessageAnswer.MessageHeader;
import ru.chirkova.vkr_asych.ModelMessageRequest.MessageRequest;

@Service
public class Producer {

    @Value("${kafka.request.topic}")
    private String requestTopic;

    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private KafkaTemplate<String, MessageRequest> kafkaTemplate;

    public void sendMessageWithCallback(MessageRequest message) {


        ListenableFuture<SendResult<String, MessageRequest>> future = kafkaTemplate.send(requestTopic, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, MessageRequest>>() {
            @Override
            public void onSuccess(SendResult<String, MessageRequest> result) {
                MessageCallBack callBack=new MessageCallBack(
                        new MessageHeader(message.getHeader().getTitle(),
                                message.getHeader().getId(),
                                message.getHeader().getTimestamp(),
                                message.getHeader().getService(),
                                message.getHeader().getCorrelationId(),
                                "Successfuly"
                                ),
                            new MessageBody(String.valueOf(result.getRecordMetadata().topic()),
                                            String.valueOf(result.getRecordMetadata().partition()),
                                            String.valueOf(result.getRecordMetadata().offset())));
                ObjectMapper om = new ObjectMapper();
                String jsonString = null;
                try {
                    jsonString = om.writerWithDefaultPrettyPrinter().writeValueAsString(callBack);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                System.out.println(jsonString);
            }

            @Override
            public void onFailure(Throwable ex) {
                ObjectMapper om = new ObjectMapper();
                String jsonString = null;
                MessageCallBack callBack=new MessageCallBack(
                        new MessageHeader(message.getHeader().getTitle(),
                                message.getHeader().getId(),
                                message.getHeader().getTimestamp(),
                                message.getHeader().getService(),
                                message.getHeader().getCorrelationId(),
                                "Error"
                        ),
                        new MessageBody(new ErrorMessage(String.valueOf(ex.getCause()),
                                        String.valueOf(ex.getMessage()))));
                try {
                    jsonString = om.writerWithDefaultPrettyPrinter().writeValueAsString(callBack);

                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                System.out.println(jsonString);
            }

        });
    }
}



