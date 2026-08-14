package com.example.demo;

import com.example.demo.ai.TaskRagService;
import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final TaskRepository taskRepository;
    private final TaskRagService taskRagService;

    // write constructor manually — @Lazy on the PARAMETER not the field
    public DataSeeder(TaskRepository taskRepository,
                      @Lazy TaskRagService taskRagService) {
        this.taskRepository = taskRepository;
        this.taskRagService = taskRagService;
    }

    @Override
    public void run(String... args) {
        if (taskRepository.count() == 0) {
            taskRepository.save(Task.builder()
                    .title("Learn Spring Data JPA")
                    .description("Understand Entities and repositories")
                    .priority(Task.Priority.HIGH)
                    .status(Task.Status.IN_PROGRESS)
                    .deadline(java.time.LocalDate.of(2026, 12, 12))
                    .build());

            taskRepository.save(Task.builder()
                    .title("Setup PostgreSQL")
                    .description("Install and connect to Spring Boot")
                    .priority(Task.Priority.HIGH)
                    .status(Task.Status.DONE)
                    .deadline(java.time.LocalDate.of(2026, 12, 12))
                    .build());

            System.out.println("Seeded 2 tasks");
        }

        taskRagService.indexAllTasks();
    }
}