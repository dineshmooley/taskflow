package com.example.demo.controller;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {

        taskRepository.deleteAll();

        taskRepository.save(Task.builder()
                .title("Test task")
                .priority(Task.Priority.MEDIUM)
                .status(Task.Status.TODO)
                .deadline(LocalDate.of(2026, 8, 1))
                .build());

    }

    @WithMockUser
    @Test
    void getAllTasks_returns200WithTasks() throws Exception {

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Test task"));


    }

    @WithMockUser
    @Test
    void createTask_returns201WithSavedTask() throws Exception {

        Task newTask = Task.builder()
                .title("New integration task")
                .priority(Task.Priority.HIGH)
                .status(Task.Status.TODO)
                .deadline(LocalDate.of(2026, 9, 1))
                .build();

        mockMvc.perform(post("/api/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New integration task"))
                .andExpect(jsonPath("$.priority").value("HIGH"));

    }

    @WithMockUser
    @Test
    void getTaskById_whenNotFound_returns500()  throws Exception {

        mockMvc.perform(get("/api/tasks/{id}", 999))
                .andExpect(status().isNotFound());

    }

    @WithMockUser
    @Test
    void getTaskById_whenNotFound_returns404() throws Exception {
        mockMvc.perform(get("/api/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Task not found with id: 999"));
    }

}
