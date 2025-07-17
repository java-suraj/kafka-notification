package com.demo.kafka.payload;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * A custom serializer for Kafka messages that uses Jackson to serialize Message objects into JSON data.
 *
 * @param <T> the type of the message payload
 */
@Slf4j
public class MessageSerializer<T> implements Serializer<KafkaMessage<T>> {

    private final ObjectMapper objectMapper;

    /**
     * Constructs a new MessageSerializer and registers the JavaTimeModule with the ObjectMapper.
     */
    public MessageSerializer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Configures the serializer. No configuration is needed for this serializer.
     *
     * @param configs the configuration settings
     * @param isKey   whether the serializer is for keys or values
     */
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No configuration needed
    }

    /**
     * Serializes the given Message object into a byte array.
     *
     * @param topic the topic associated with the data
     * @param data  the Message object to serialize
     * @return the serialized bytes
     */
    @Override
    public byte[] serialize(String topic, KafkaMessage<T> data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            log.error("The message type not supported, error on serializing message", e);
        }
        return new byte[0];
    }

    /**
     * Closes the serializer. No resources need to be closed for this serializer.
     */
    @Override
    public void close() {
        // No resources to close
    }
}