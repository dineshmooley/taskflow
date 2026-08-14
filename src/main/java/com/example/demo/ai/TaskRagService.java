package com.example.demo.ai;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskRagService {

    private final VectorStore vectorStore;
    private final TaskRepository taskRepository;
    private final ChatClient.Builder chatClientBuilder;

    private ChatClient chatClient;

    @PostConstruct
    public void init() {

        this.chatClient = chatClientBuilder.build();

    }

    // Call this when a task is created or updated
    public void indexTask(Task task) {
        String content = String.format(
                "Task: %s. Description: %s. Priority: %s. Status: %s. Deadline: %s",
                task.getTitle(),
                task.getDescription() != null ? task.getDescription() : "none",
                task.getPriority(),
                task.getStatus(),
                task.getDeadline()
        );

        Document document = new Document(
                content,
                Map.of("taskId", task.getId().toString())
        );

        vectorStore.add(List.of(document));
        log.info("Indexed task {} in vector store", task.getId());
    }

    // Search tasks using natural language
    public List<Task> searchTasks(String naturalLanguageQuery) {
        List<Document> results = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(naturalLanguageQuery)
                        .topK(5)
                        .build()
        );

        return results.stream()
                .map(doc -> {
                    Long taskId = Long.parseLong(
                            doc.getMetadata().get("taskId").toString()
                    );
                    return taskRepository.findById(taskId).orElse(null);
                })
                .filter(task -> task != null)
                .collect(Collectors.toList());
    }

    // Index all existing tasks (call once on startup)
    public void indexAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        tasks.forEach(this::indexTask);
        log.info("Indexed {} tasks in vector store", tasks.size());
    }

    public String chat(String userQuestion) {

        List<Document> relevantDocs = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(userQuestion)
                        .topK(5)
                        .build()
        );

        String context = relevantDocs
                .stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n"));

        String prompt = """
                You are a helpful task management assistant.
               
                Here are the user's relevant tasks:
                %s
               
                Based on these tasks, answer the following question:
                %s
               
                Be concise and helpful. If recommending tasks to work on,
                explain briefly why.
               """.formatted(context, userQuestion);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();

    }
}
