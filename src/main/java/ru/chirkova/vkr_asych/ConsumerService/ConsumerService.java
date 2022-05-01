package ru.chirkova.vkr_asych.ConsumerService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.chirkova.vkr_asych.ModelMessageRequest.MessageRequest;

@Slf4j
@Service
public class ConsumerService {
    @KafkaListener(topics = "${kafka.request.topic}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void listen(MessageRequest request) {
        if (String.valueOf(request.getHeader().getId()).length() ==0 ) {        //  Вызов исключения для проверки записи сообщений в отдельнвый топик при ошибки потребления сообщения потребителем
            log.info("The id field cannot be null");
            throw new NullPointerException("The id field cannot be null");
        }
        else
            log.info("Processed " +request);
    }
}
