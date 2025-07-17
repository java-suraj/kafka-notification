package com.demo.kafka.config;

import com.demo.kafka.utils.GlobalConstant;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.internals.Topic;
import org.apache.logging.log4j.util.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationPostConfig {

    private final KafkaEventService<String> kafkaServiceConfig;

    public ApplicationPostConfig(KafkaEventService<String> kafkaServiceConfig) {
        this.kafkaServiceConfig = kafkaServiceConfig;
    }

    @PostConstruct
    public void init() {
        kafkaServiceConfig.createTopic(GlobalConstant.TOPIC_NAME, 3, (short) 1);
    }
}
