package com.demo.kafka.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * A generic Kafka message wrapper containing metadata and a payload.
 *
 * @param <T> the type of the message payload
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class KafkaMessage<T> {

    /**
     * Unique ID of the message, often used for deduplication or reference.
     */
    private String messageId;

    /**
     * Correlation ID used for tracking and tracing messages across systems.
     */
    private String correlationId;

    /**
     * Type or category of the message (e.g., EVENT, COMMAND, QUERY).
     */
    private String type;

    /**
     * Operation this message represents (e.g., CREATE, UPDATE, DELETE, NOTIFY).
     */
    private String operation;

    /**
     * Name of the service, application, or system that produced the message.
     */
    private String source;

    /**
     * Status or processing success flag (e.g., true if processed successfully).
     */
    private Boolean success;

    /**
     * Version identifier of the message or payload schema.
     */
    private String version;

    /**
     * Priority of the message (e.g., HIGH, MEDIUM, LOW); may influence processing order or retries.
     */
    private String priority;

    /**
     * Human-readable log or trace message, useful for debugging or audits.
     */
    private String messageLog;

    /**
     * The timestamp when the message was created or sent.
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    /**
     * The expiration time of the message, after which it may be discarded or ignored.
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expirationTime;

    /**
     * Flexible headers for custom metadata (e.g., region, tenant, auth token).
     */
    private Map<String, String> headers;

    /**
     * The actual business payload being transmitted.
     */
    private T payload;
}
