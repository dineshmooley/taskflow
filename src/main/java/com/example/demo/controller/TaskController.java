package com.example.demo.controller;

import com.example.demo.ai.TaskRagService;
import com.example.demo.entity.Task;
import com.example.demo.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskRagService taskRagService;

    @GetMapping
    public List<Task> getAllTasks(@RequestParam(required = false) String sortBy) {
        return taskService.getAllTasks(sortBy);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        Task saved = taskService.createTask(task);
        taskRagService.indexTask(saved);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody Task task) {

        Task saved = taskService.updateTask(id, task);
        taskRagService.indexTask(saved);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public List<Task> getTasksByStatus(@PathVariable Task.Status status) {
        return taskService.getTaskByStatus(status);
    }

    @GetMapping("/priority/{priority}")
    public List<Task> getTasksByPriority(@PathVariable Task.Priority priority) {
        return taskService.getTasksByPriority(priority);
    }

    @GetMapping("/search")
    public List<Task> getTasksByTitle(@RequestParam String title) {
        return taskService.searchByTitle(title);
    }

    @GetMapping("/overdue")
    public List<Task> getOverdueTasks() {
        return taskService.getOverdueTasks();
    }

}
