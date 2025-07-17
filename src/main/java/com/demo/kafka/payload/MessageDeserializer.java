package com.demo.kafka.payload;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * A custom deserializer for Kafka messages that uses Jackson to deserialize JSON data into Message objects.
 *
 * @param <T> the type of the message payload
 */
@Slf4j
public class MessageDeserializer<T> implements Deserializer<KafkaMessage<T>> {

    private final ObjectMapper objectMapper;

    /**
     * Constructs a new MessageDeserializer and registers the JavaTimeModule with the ObjectMapper.
     */
    public MessageDeserializer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Configures the deserializer. No configuration is needed for this deserializer.
     *
     * @param configs the configuration settings
     * @param isKey   whether the deserializer is for keys or values
     */
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No configuration needed
    }

    /**
     * Deserializes the given byte array into a Message object.
     *
     * @param topic the topic associated with the data
     * @param data  the serialized bytes
     * @return the deserialized Message object
     */
    @Override
    public KafkaMessage<T> deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(data, KafkaMessage.class);
        } catch (Exception e) {
            log.error("The message type not supported, error on deserializing message", e);
        }
        return new KafkaMessage<>();
    }

    /**
     * Closes the deserializer. No resources need to be closed for this deserializer.
     */
    @Override
    public void close() {
        // No resources to close
    }
}