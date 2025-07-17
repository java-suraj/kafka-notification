package com.demo.kafka.service;

import com.demo.kafka.config.KafkaEventService;
import com.demo.kafka.payload.ApiResponse;
import com.demo.kafka.payload.KafkaMessage;
import com.demo.kafka.utils.GlobalConstant;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;


@Service
@Data
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaEventService<?> kafkaServiceConfig;

    public ApiResponse sendMessage(KafkaMessage<String> message) {
        try {
            kafkaServiceConfig.publishEvent(GlobalConstant.TOPIC_NAME, String.valueOf(UUID.randomUUID()), message);
            return ApiResponse.success("Message sent successfully",null);
        } catch (Exception e) {
            return ApiResponse.error("Error while sending message", Collections.singletonList(e.getMessage()));
        }

    }
}
