package com.example.demo.service;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestTaskService {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTask;

    @BeforeEach
    void beforeEach() {

        sampleTask = Task.builder()
                .title("Write JUnit tests")
                .id(1L)
                .status(Task.Status.TODO)
                .deadline(LocalDate.of(2026,6,30))
                .priority(Task.Priority.HIGH)
                .description("Cover the service Layer")
                .build();

    }

    @Test
    void getAllTasks_returnsListOfTasks() {

        when(taskRepository.findAll()).thenReturn(List.of(sampleTask));

        List<Task> result = taskService.getAllTasks(null);

        assertEquals(1, result.size());
        assertEquals("Write JUnit tests", result.getFirst().getTitle());

        verify(taskRepository).findAll();

    }

    @Test
    void getTaskById_ifExists_returnsTask() {

        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));

        Task result = taskService.getTaskById(1L);

        assertEquals(1L, result.getId());
        assertEquals(Task.Status.TODO, result.getStatus());

    }

    @Test
    void getTaskById_ifNotExists_ThrowsException() {

        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.getTaskById(999L));

    }

    @Test
    void createTask_savesAndReturnsTask() {

        when(taskRepository.save(sampleTask)).thenReturn(sampleTask);

        Task result = taskService.createTask(sampleTask);

        assertEquals("Write JUnit tests", result.getTitle());
        verify(taskRepository).save(sampleTask);

    }

    @Test
    void deleteTask_callsDeletesTask() {

        taskService.deleteTaskById(1L);

        verify(taskRepository).deleteById(1L);

    }

    @Test
    void getTasksByStatus_returnsListOfTasks() {

        sampleTask.setStatus(Task.Status.DONE);

        when(taskRepository.findByStatus(Task.Status.DONE)).thenReturn(List.of(sampleTask));

        List<Task> result = taskService.getTaskByStatus(Task.Status.DONE);

        assertEquals(1, result.size());
        assertEquals(Task.Status.DONE, result.getFirst().getStatus());

        verify(taskRepository).findByStatus(Task.Status.DONE);

    }
}
