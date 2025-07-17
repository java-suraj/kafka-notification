package com.demo.kafka.config;

import com.demo.kafka.payload.KafkaMessage;
import com.demo.kafka.utils.GlobalConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
@Slf4j
@Component
public class KafkaWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("New WebSocket connection established: {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("WebSocket connection closed: {} with status {}", session.getId(), status);
    }

    @KafkaListener(topics = GlobalConstant.TOPIC_NAME)
    public void listen(KafkaMessage<String> message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            log.info("Sending message to {} WebSocket sessions: {}", sessions.size(), json);
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(json));
                    log.info("Message sent to session: {}", session.getId());
                }
            }
        } catch (Exception e) {
            log.error("Error sending message to WebSocket session: {}", e.getMessage());
        }
    }
}