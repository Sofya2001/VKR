package ru.chirkova.vkr_asych.ConsumerService;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import ru.chirkova.vkr_asych.ConsumerService.Error.KafkaErrorHandler;
import ru.chirkova.vkr_asych.ModelMessageRequest.MessageRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@EnableKafka
@Configuration
public class ConsumerConfig {
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${kafka.dlq.topic}")
    private String topicError;

    @Autowired
    private  KafkaTemplate<String, MessageRequest> kafkaTemplate;


    @Bean
    public ConsumerFactory<String, MessageRequest> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);

        ErrorHandlingDeserializer<MessageRequest> errorHandlingDeserializer
                =new ErrorHandlingDeserializer<>(new JsonDeserializer<>(MessageRequest.class));
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                errorHandlingDeserializer
        );
    }



    @Bean("kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, MessageRequest>
    kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, MessageRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setRetryTemplate(kafkaRetry());
        factory.setRecoveryCallback(this::retryOption);
        factory.setErrorHandler(new KafkaErrorHandler());
        return factory;
    }

    private RetryTemplate kafkaRetry() {
        RetryTemplate retryTemplate = new RetryTemplate();
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(5*1000L);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);
        return retryTemplate;
    }

    private Object retryOption(RetryContext retryContext){
        ConsumerRecord<String,MessageRequest> consumerRecord=(ConsumerRecord) retryContext.getAttribute("record");
        MessageRequest messageRequest=consumerRecord.value();
        log.info("Recovery is called for message {} ", messageRequest);
        doOnRetry(retryContext,consumerRecord,messageRequest);
            return Optional.empty();
        }

    private void doOnRetry(RetryContext retryContext, ConsumerRecord<String, MessageRequest> consumerRecord, MessageRequest request) {
        if (Boolean.TRUE.equals(retryContext.getAttribute(RetryContext.EXHAUSTED))) {
            log.info("MOVED TO ERROR DLQ");
            kafkaTemplate.send( topicError,
                    consumerRecord.key(),
                    consumerRecord.value() );
        }
    }






}

