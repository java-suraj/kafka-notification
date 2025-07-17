package com.demo.kafka.controller;

import com.demo.kafka.payload.ApiResponse;
import com.demo.kafka.payload.KafkaMessage;
import com.demo.kafka.service.KafkaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka")
@RequiredArgsConstructor
public class KafkaController {
    private final KafkaService kafkaService;

    @PostMapping("/send")
    public ApiResponse sendMessage(@RequestBody KafkaMessage<String> message) {
        return kafkaService.sendMessage(message);
    }

    @GetMapping("/health")
    public ApiResponse healthCheck() {
        return ApiResponse.success("Kafka service is running", null);
    }
}
