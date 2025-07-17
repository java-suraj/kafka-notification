package com.demo.kafka.config;

import com.demo.kafka.payload.KafkaMessage;
import com.demo.kafka.utils.GlobalConstant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaEventService<T> {

    private final KafkaTemplate<String, KafkaMessage<?>> kafkaTemplate;
    private final KafkaAdmin kafkaAdmin;
    private final ConsumerFactory<String, KafkaMessage<?>> consumerFactory;
    @Getter
    private final KafkaConfig kafkaConfig;

    /**
     * Creates a new Kafka topic.
     *
     * @param topicName         the name of the topic
     * @param partitions        the number of partitions
     * @param replicationFactor the replication factor
     */
    public void createTopic(String topicName, int partitions, short replicationFactor) {
        if (GlobalConstant.isKafkaEnabled()) {
            NewTopic topic = new NewTopic(topicName, partitions, replicationFactor);
            kafkaAdmin.createOrModifyTopics(topic);
        }
    }

    /**
     * Deletes an existing Kafka topic.
     *
     * @param topicName the name of the topic
     */
    public void deleteTopic(String topicName) {
        if (GlobalConstant.isKafkaEnabled()) {
            try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
                adminClient.deleteTopics(Collections.singletonList(topicName)).all().get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("Exception while deleting topic: {}", e.getMessage());
            }
        }
    }

    /**
     * Clears a Kafka topic by deleting and recreating it.
     *
     * @param topicName the name of the topic
     */
    public void clearTopic(String topicName) {
        if (GlobalConstant.isKafkaEnabled()) {
            deleteTopic(topicName);
            createTopic(topicName, 1, (short) 1);
        }
    }

    /**
     * Publishes an event to a Kafka topic.
     *
     * @param topicName the name of the topic
     * @param key       the key of the message
     * @param value     the message payload
     */
    public void publishEvent(String topicName, String key, KafkaMessage<?> value) {
        if (GlobalConstant.isKafkaEnabled()) {
            kafkaTemplate.send(new ProducerRecord<>(topicName, key, value));
        }
    }

    /**
     * Consumes events from a Kafka topic.
     *
     * @param topicName       the name of the topic
     * @param groupId         the group ID for the consumer
     * @param clientSuffixId  the client suffix ID for the consumer
     * @param messageListener the message listener to handle consumed messages
     */
    public void consumeEvents(String topicName, String groupId, String clientSuffixId,
                              MessageListener<String, KafkaMessage<T>> messageListener) {
        if (GlobalConstant.isKafkaEnabled()) {
            ContainerProperties containerProps = new ContainerProperties(topicName);
            containerProps.setGroupId(groupId);
            containerProps.setClientId(clientSuffixId);
            containerProps.setMessageListener(messageListener);
            KafkaMessageListenerContainer<String, KafkaMessage<T>> container = new KafkaMessageListenerContainer<>(
                    consumerFactory, containerProps);
            container.start();
        }
    }

//    @KafkaListener(topics = GlobalConstant.TOPIC_NAME)
//    public void listen(KafkaMessage<?> message) {
//        log.info("Received message: {}", message);
//    }

    public boolean topicExists(String topicName) {
        try (AdminClient adminClient = AdminClient.create(kafkaConfig.kafkaProperties())) {
            ListTopicsOptions options = new ListTopicsOptions();
            options.listInternal(false);
            Set<String> topicNames = adminClient.listTopics(options).names().get();
            return topicNames.contains(topicName);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error checking topic existence: {}", e.getMessage());
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
