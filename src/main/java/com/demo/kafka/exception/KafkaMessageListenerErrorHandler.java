package com.demo.kafka.exception;

import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * A custom error handler for Kafka listener errors.
 */
@Component
public class KafkaMessageListenerErrorHandler implements KafkaListenerErrorHandler {

	/**
	 * Handles an error that occurs during message consumption in a Kafka listener.
	 *
	 * @param message   the message that caused the error
	 * @param exception the exception that was thrown
	 * @return null
	 */
	@Override
	public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
		// Log the error message
		System.err.println("Error consuming message: " + message.getPayload());
		exception.printStackTrace();
		return null;
	}
}