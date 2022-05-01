package ru.chirkova.ProducerService;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
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
import ru.chirkova.ModelMessageAnswer.MessageAnswer;
import ru.chirkova.ModelMessageRequest.MessageRequest;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProducerConfig {

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;
    @Value("${kafka.reply.topic}")
    private String replyTopic;
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
    public Map<String,Object> consumerConfigs() {
        Map<String,Object> props=new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);


        return props;
    }

    @Bean
    public ProducerFactory<String, MessageRequest> producerFactory(){
        return  new DefaultKafkaProducerFactory<>(producerConfigs());
    }



    @Bean
    public ReplyingKafkaTemplate<String,MessageRequest, MessageAnswer> replyingKafkaTemplate(
            ProducerFactory<String,MessageRequest> pf,
            KafkaMessageListenerContainer<String,MessageAnswer> container) {

        return  new ReplyingKafkaTemplate<>(pf,container);
    }

    @Bean
    public ConsumerFactory<String,MessageAnswer> consumerFactory() {
        JsonDeserializer<MessageAnswer> deserializer=new JsonDeserializer<>(MessageAnswer.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        ErrorHandlingDeserializer<MessageAnswer> errorHandlingDeserializer
                =new ErrorHandlingDeserializer<>(deserializer);

        return new DefaultKafkaConsumerFactory<>(consumerConfigs(),new StringDeserializer(),errorHandlingDeserializer);

    }

    @Bean
    public KafkaMessageListenerContainer<String,MessageAnswer> replyContainer(ConsumerFactory<String,MessageAnswer> cf){
        ContainerProperties containerProperties= new ContainerProperties(replyTopic);
        return  new KafkaMessageListenerContainer<>(cf,containerProperties);

    }

}

