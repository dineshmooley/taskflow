package com.example.demo.service;

import com.example.demo.entity.Task;
import com.example.demo.exception.TaskNotFoundException;
import com.example.demo.kafka.TaskEventProducer;
import com.example.demo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskEventProducer taskEventProducer;

    public List<Task> getAllTasks(String sortBy) {

        if(sortBy != null)  {
            return taskRepository.findAll(Sort.by(sortBy));
        }

        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task existing = getTaskById(id);
        existing.setTitle(updatedTask.getTitle());
        existing.setDescription(updatedTask.getDescription());
        existing.setPriority(updatedTask.getPriority());
        existing.setStatus(updatedTask.getStatus());
        existing.setDeadline(updatedTask.getDeadline());

        Task saved = taskRepository.save(existing);

        // Publish event if task was just marked DONE
        if (saved.getStatus() == Task.Status.DONE) {
            taskEventProducer.publishTaskCompleted(saved.getId(), saved.getTitle());
        }

        return saved;
    }

    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    public List<Task> getTaskByStatus(Task.Status status) {

        return taskRepository.findByStatus(status);

    }

    public List<Task> getTasksByPriority(Task.Priority priority) {
        return taskRepository.findByPriority(priority);
    }

//    public List<Task> getByPriorityAndStatus(Task.Priority priority, Task.Status status) {
//        return taskRepository.findByPriorityAndStatus(priority, status);
//    }

    public List<Task> searchByTitle(String title) {
        return taskRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Task> getOverdueTasks() {
        return taskRepository.findOverdueTasks();
    }

}
