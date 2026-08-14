package com.example.demo.controller;


import com.example.demo.ai.TaskRagService;
import com.example.demo.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final TaskRagService taskRagService;

    @GetMapping("/search")
    public List<Task> naturalLanguageSearch(@RequestParam String query) {
        return taskRagService.searchTasks(query);
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String question) {

        return taskRagService.chat(question);

    }
}
