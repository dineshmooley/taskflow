package com.example.demo.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TaskEventConsumer {

    @KafkaListener(topics = "task-completed", groupId = "taskflow-group")
    public void handleTaskCompleted(String message) {
        log.info("Received Kafka event: {}", message);
        // In a real app: send email, push notification, update analytics, etc.
    }
}
