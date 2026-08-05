package com.example.demo.repository;

import com.example.demo.entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();

        taskRepository.save(Task.builder()
                .title("Fix login bug")
                .priority(Task.Priority.HIGH)
                .status(Task.Status.TODO)
                .deadline(LocalDate.of(2026, 12, 1))
                .build());

        taskRepository.save(Task.builder()
                .title("Write documentation")
                .priority(Task.Priority.LOW)
                .status(Task.Status.DONE)
                .deadline(LocalDate.of(2026, 12, 15))
                .build());
    }

    @WithMockUser
    @Test
    void findByStatus_returnsDoneTasks()    {

        List<Task> result = taskRepository.findByStatus(Task.Status.DONE);

        assertEquals(1, result.size());
        assertEquals("Write documentation", result.getFirst().getTitle());


    }

    @WithMockUser
    @Test
    void findByTitleContainingIgnoreCase_returnsMatch() {

        List<Task> result = taskRepository.findByTitleContainingIgnoreCase("login");

        assertEquals(1, result.size());
        assertEquals(Task.Priority.HIGH, result.getFirst().getPriority());

    }

//    @Test
//    void findOverDueTasks_returnsOnlyUnfinishedOverdue()    {
//
//        List<Task> result = taskRepository.findOverdueTasks();
//
//        assertEquals(1, result.size());
//        assertEquals("Fix login bug", result.getFirst().getTitle());
//
//    }
//Validated the above test, since we are using @Future validiation now in Entity and we are entering the data as the application is loading, we cannot achieve this.
}
