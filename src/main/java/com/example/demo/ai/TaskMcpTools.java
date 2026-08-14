package com.example.demo.ai;

import com.example.demo.entity.Task;
import com.example.demo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskMcpTools {

    private final TaskService taskService;

    @Tool(description = "Get all tasks in the system")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks(null);
    }

    @Tool(description = "Get tasks filtered by status. Status can be TODO, IN_PROGRESS, or DONE")
    public List<Task> getTasksByStatus(
            @ToolParam(description = "The status to filter by: TODO, IN_PROGRESS, or DONE")
            String status) {
        return taskService.getTasksByStatus(Task.Status.valueOf(status));
    }

    @Tool(description = "Get tasks filtered by priority. Priority can be LOW, MEDIUM, or HIGH")
    public List<Task> getTasksByPriority(
            @ToolParam(description = "The priority to filter by: LOW, MEDIUM, or HIGH")
            String priority) {
        return taskService.getTasksByPriority(Task.Priority.valueOf(priority));
    }

    @Tool(description = "Search tasks using natural language query")
    public List<Task> searchTasks(
            @ToolParam(description = "Natural language search query")
            String query) {
        return taskService.getTaskByKeywordIgnoreCase(query);
    }

    @Tool(description = "Create a new task")
    public Task createTask(
            @ToolParam(description = "Title of the task") String title,
            @ToolParam(description = "Description of the task") String description,
            @ToolParam(description = "Priority: LOW, MEDIUM, or HIGH") String priority,
            @ToolParam(description = "Deadline in format YYYY-MM-DD") String deadline) {

        Task task = Task.builder()
                .title(title)
                .description(description)
                .priority(Task.Priority.valueOf(priority))
                .status(Task.Status.TODO)
                .deadline(LocalDate.parse(deadline))
                .build();

        return taskService.createTask(task);
    }

    @Tool(description = "Get overdue tasks that are not yet completed")
    public List<Task> getOverdueTasks() {
        return taskService.getOverdueTasks();
    }
}