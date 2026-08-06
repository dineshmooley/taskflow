# TaskFlow API

A production-ready Task Management REST API built with Spring Boot.

## Tech Stack
- Java 21 + Spring Boot 3.4
- PostgreSQL + Spring Data JPA
- Spring Security + JWT Authentication
- Apache Kafka (event streaming)
- JUnit 5 + Mockito (unit & integration tests)

## Features
- Full CRUD for tasks with filtering by status/priority
- Natural language title search
- Overdue task detection
- JWT-based authentication (register/login)
- Kafka event published when a task is marked DONE
- Global exception handling with meaningful error responses
- Input validation with field-level error messages

## Getting Started

### Prerequisites
- Java 21
- PostgreSQL running on port 5432
- Kafka running on port 9092

### Setup
1. Clone the repo
2. Create a PostgreSQL database called `taskflow`
3. Create `src/main/resources/application-local.properties`:
