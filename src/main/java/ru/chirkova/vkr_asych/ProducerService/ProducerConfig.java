package ru.chirkova.vkr_asych.ProducerService;

import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.chirkova.vkr_asych.ModelMessageRequest.MessageRequest;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class ProducerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerConfig.class);


    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;


    @Bean
    public Map<String,Object> producerConfigs() {
        Map<String,Object> props=new HashMap<>();
        props.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return props;
    }


    @Bean
    public ProducerFactory<String, MessageRequest> producerFactory(){
        return  new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String,MessageRequest> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }


}
