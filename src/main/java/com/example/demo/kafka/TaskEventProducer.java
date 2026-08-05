package com.example.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskEventProducer {

    private static final String TOPIC = "task-completed";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publishTaskCompleted(Long taskId, String title) {
        String message = "Task DONE | id=" + taskId + " | title=" + title;
        kafkaTemplate.send(TOPIC, message);
        log.info("Published to Kafka: {}", message);
    }
}