# GC Events

GC Events is a Java-based project that uses Kafka for messaging. It is built with Java 17, Spring 6, and Spring Boot 3.
This project provides a simple way to configure Kafka, create Kafka-related beans, and publish and consume events.

## Prerequisites

- Java 17
- Maven
- Kafka

## Dependencies

This project uses the following main dependencies:

- Spring Boot Starter Web
- Spring Boot DevTools
- Spring Boot Configuration Processor
- Lombok
- Spring Boot Starter Test
- Jackson Databind
- Jackson Datatype JSR310
- Apache Kafka Clients
- Spring Kafka

Add these dependencies in your `pom.xml` inside the `<dependencies>` section.

## Getting Started

### Configuration

Add the following properties to your `application.properties` file:

```ini
kafka.bootstrap-servers=localhost:9092 # Kafka server address, required property
kafka.consumer.group-id=default-group # ConsumerGroup-ID, required property
kafka.consumer.auto-offset-reset=earliest
kafka.consumer.enable-auto-commit=true
kafka.consumer.auto-commit-interval-ms=1000
kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
kafka.consumer.properties.max.poll.records=1
kafka.consumer.properties.max.poll.interval.ms=1000
kafka.consumer.properties.max.partition-fetch.bytes=1048576
kafka.consumer.properties.fetch.max.bytes=1048576
kafka.consumer.properties.fetch.min.bytes=1
kafka.consumer.properties.fetch.max
```

### Initialize Kafka Beans

In your Spring Boot application, you can initialize Kafka beans by adding the following code:

```java
@ComponentScan(basePackages = "com.gc.messaging")
```

### Create & Delete a Kafka Topic

1. **Autowire KafkaService**: Ensure that `KafkaService` is autowired in your Spring service or component.

    ```java
    @Autowired
    private KafkaService<?> kafkaService;
    ```

2. Use the `createTopic` and `deleteTopic` methods to create and delete Kafka topic. Provide the topic name, number of
   partitions, and replication factor.

    ```java
    kafkaService.createTopic("my-topic", 3, (short) 1);
    ```
    ```java
    kafkaService.deleteTopic("my-topic");
    ```

### Publish Events

To publish events, you can use the `kafkaService` class. Here is an example:

```java
    kafkaService.publish("topic","message");
```

### Consume Events

To consume events, you can use the `kafkaService` class. Here is an example:

```java

@KafkaListener(topics = "test-topic", groupId = "${kafka.consumer.group-id}", errorHandler = "listenerErrorHandler")
public void listen(Message<String> message) {
    System.out.println("Received message: " + message);
}
```


