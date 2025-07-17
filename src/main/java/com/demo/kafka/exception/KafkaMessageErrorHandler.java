package com.demo.kafka.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageErrorHandler implements CommonErrorHandler {
	@Override
	@Autowired
	public boolean seeksAfterHandling() {
		return true;
	}
}