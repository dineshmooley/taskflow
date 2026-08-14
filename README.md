# TaskFlow API
A production-ready Task Management REST API built with Spring Boot, featuring AI-powered natural language search, an LLM chat interface, and an MCP server for AI agent integration.

## Tech Stack
- Java 21 + Spring Boot 3.4
- PostgreSQL + pgvector + Spring Data JPA
- Spring Security + JWT Authentication
- Apache Kafka (event streaming)
- Spring AI 1.0 + Ollama (Llama 3.2 + nomic-embed-text)
- MCP Server (Spring AI MCP)
- JUnit 5 + Mockito (unit & integration tests)

## Features
- Full CRUD for tasks with filtering by status/priority
- Natural language title search
- Overdue task detection
- JWT-based authentication (register/login)
- Kafka event published when a task is marked DONE
- Global exception handling with meaningful error responses
- Input validation with field-level error messages
- RAG-powered semantic search using pgvector embeddings
- AI chat endpoint — ask questions about your tasks in plain English
- MCP server exposing 6 tools to AI agents like Claude

## Getting Started

### Prerequisites
- Java 21
- PostgreSQL running on port 5432
- Kafka running on port 9092
- Ollama running locally with `llama3.2` and `nomic-embed-text` models

### Setup
1. Clone the repo
2. Create a PostgreSQL database called `taskflow`
3. Enable pgvector extension in psql:
```sql
   CREATE EXTENSION IF NOT EXISTS vector;
```
4. Create `src/main/resources/application-local.properties`
```application.properites
spring.datasource.url=jdbc:postgresql://localhost:5432/taskflow
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.ai.openai.api-key=your_key
```
5. Pull Ollama models:
```bash
   ollama pull llama3.2
   ollama pull nomic-embed-text
```
6. Run: `./mvnw spring-boot:run`

## API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Register new user |
| POST | /api/auth/login | Login and get JWT token |

### Tasks (require Bearer token)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/tasks | Get all tasks |
| GET | /api/tasks/{id} | Get task by ID |
| POST | /api/tasks | Create task |
| PUT | /api/tasks/{id} | Update task |
| DELETE | /api/tasks/{id} | Delete task |
| GET | /api/tasks/status/{status} | Filter by status |
| GET | /api/tasks/priority/{priority} | Filter by priority |
| GET | /api/tasks/search?keyword= | Search by title |
| GET | /api/tasks/overdue | Get overdue tasks |

### AI (require Bearer token)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/ai/search?query= | Semantic search using natural language |
| GET | /api/ai/chat?question= | Ask questions about your tasks in plain English |

### MCP Server
| Endpoint | Description |
|----------|-------------|
| /sse | MCP server endpoint for AI agent connections |

#### Available MCP Tools
| Tool | Description |
|------|-------------|
| getAllTasks | Get all tasks in the system |
| getTasksByStatus | Filter tasks by TODO, IN_PROGRESS, or DONE |
| getTasksByPriority | Filter tasks by LOW, MEDIUM, or HIGH |
| searchTasks | Semantic search using natural language |
| createTask | Create a new task |
| getOverdueTasks | Get all overdue incomplete tasks |

## Architecture

```mermaid
flowchart TD
    A[Controller] --> B[Service]
    B --> C[Repository]
    C --> D[(PostgreSQL)]

    B --> E[Kafka Producer]
    E --> F[task-completed topic]
    F --> G[Kafka Consumer]

    G --> H[TaskRagService]
    H --> I[pgvector<br/>Embeddings]
    I --> J[Ollama<br/>Llama 3.2]

    H --> K[MCP Server]
    K --> L[AI Agents<br/>Claude / GitHub Copilot]
