package ru.chirkova.ConsumerService;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import ru.chirkova.ConsumerService.Error.KafkaErrorHandler;
import ru.chirkova.ModelMessageAnswer.MessageAnswer;
import ru.chirkova.ModelMessageRequest.MessageRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Configuration
public class ConsumerConfig {


    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;
    @Value("${kafka.reply.topic}")
    private String replyTopic;
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${kafka.dlq.topic}")
    private String topicError;


    private  KafkaTemplate<String, MessageRequest> kafkaTemplate;


    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return props;
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        return props;
    }

    @Bean
    public ConsumerFactory<String, MessageRequest> RconsumerFactory() {
        JsonDeserializer<MessageRequest> deserializer = new JsonDeserializer<>(MessageRequest.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);
        ErrorHandlingDeserializer<MessageRequest> errorHandlingDeserializer
                =new ErrorHandlingDeserializer<>(deserializer);

        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), errorHandlingDeserializer);

    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, MessageRequest>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MessageRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<String, MessageRequest>();
        factory.setConsumerFactory(RconsumerFactory());
        factory.setReplyTemplate(RkafkaTemplate());
        factory.setErrorHandler(new KafkaErrorHandler());
        factory.setRetryTemplate(kafkaRetry());
        factory.setRecoveryCallback(this::retryOption);
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

    @Bean
    public ProducerFactory<String, MessageAnswer> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, MessageAnswer> RkafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
