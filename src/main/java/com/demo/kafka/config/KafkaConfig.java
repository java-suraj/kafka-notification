package com.demo.kafka.config;

import com.demo.kafka.exception.KafkaMessageErrorHandler;
import com.demo.kafka.exception.KafkaMessageListenerErrorHandler;
import com.demo.kafka.payload.KafkaMessage;
import com.demo.kafka.payload.MessageDeserializer;
import com.demo.kafka.payload.MessageSerializer;
import com.demo.kafka.utils.GlobalConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.demo.kafka.utils.GlobalConstant.getBootstrapServer;
import static com.demo.kafka.utils.GlobalConstant.getGroup;

@Configuration
@EnableKafka
@Slf4j
@RequiredArgsConstructor
public class KafkaConfig {

    /**
     * Creates a KafkaAdmin bean to manage Kafka topics.
     *
     * @return KafkaAdmin instance
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        if (GlobalConstant.isKafkaEnabled()) {
            configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, getBootstrapServer());
            configs.put(AdminClientConfig.RETRIES_CONFIG, 1);
            configs.put(AdminClientConfig.RETRY_BACKOFF_MS_CONFIG, 1000L);
        }
        return new KafkaAdmin(configs);
    }

    /**
     * Creates an ErrorHandler bean to handle errors during message consumption.
     *
     * @return ErrorHandler instance
     */
    @Bean
    public CommonErrorHandler errorHandler() {
        return new KafkaMessageErrorHandler();
    }

    /**
     * Creates a KafkaListenerErrorHandler bean to handle errors in Kafka listeners.
     *
     * @return KafkaListenerErrorHandler instance
     */
    @Bean
    public KafkaListenerErrorHandler listenerErrorHandler() {
        return new KafkaMessageListenerErrorHandler();
    }

    /**
     * Creates a ProducerFactory bean to produce messages to Kafka.
     *
     * @return ProducerFactory instance
     */
    @Bean
    public ProducerFactory<String, KafkaMessage<?>> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, getBootstrapServer());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MessageSerializer.class);
        configProps.put(ProducerConfig.RETRIES_CONFIG, 1);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Creates a KafkaTemplate bean to send messages to Kafka.
     *
     * @return KafkaTemplate instance
     */
    @Bean
    public KafkaTemplate<String, KafkaMessage<?>> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * Creates a ConsumerFactory bean to consume messages from Kafka.
     *
     * @return ConsumerFactory instance
     */
    @Bean
    public ConsumerFactory<String, KafkaMessage<?>> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, getBootstrapServer());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, getGroup());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, 1000L);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public Properties kafkaProperties() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, getBootstrapServer());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, getGroup());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, 1000L);
        return properties;
    }

    /**
     * Creates a ConcurrentKafkaListenerContainerFactory bean to manage Kafka
     * listener containers.
     *
     * @return ConcurrentKafkaListenerContainerFactory instance
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaMessage<?>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, KafkaMessage<?>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setCommonErrorHandler(errorHandler());
        return factory;
    }

}
