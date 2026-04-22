# Candidate Flow

Candidate Flow is a hiring pipeline dashboard built for backend-focused portfolio presentation. The project combines a Spring Boot API with a React dashboard and demonstrates layered backend design, relational modeling, validation, CRUD operations, and live pipeline tracking.

## Project Description

Candidate Flow is a recruitment pipeline management application developed to streamline candidate tracking, job posting management, and hiring stage progression. The backend is built with Spring Boot using layered architecture, DTO-based request and response handling, validation, and JPA/Hibernate for data persistence. The frontend is a React dashboard that presents hiring metrics, candidate data, open roles, and live application status updates in a responsive interface.

## GitHub Short Description

Backend-first hiring pipeline dashboard built with Spring Boot, React, JPA, and H2 in MSSQL mode.

## Stack

- Backend: Java 21, Spring Boot 3.5, Spring Data JPA, Spring Security, Validation
- Database: H2 in MSSQL compatibility mode
- Frontend: React 19, Vite
- Testing: JUnit 5, MockMvc

## Core Features

- Candidate creation with source, stack, experience, and notice period details
- Job posting management with team, level, employment type, and salary band
- Application pipeline with status transitions from `NEW` to `HIRED` or `REJECTED`
- Dashboard metrics for candidate count, open roles, live pipeline, and interview-ready pool
- Seed data for immediate demo without manual setup
- Global error handling and request validation

## Project Structure

- `backend`: Spring Boot REST API
- `frontend`: React dashboard

## Run Locally

### Requirements

- Java 21 or newer
- Node.js 20+
- npm

### 1. Start the backend

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

The backend starts on `http://localhost:8080`

### 2. Start the frontend in a second terminal

```powershell
cd frontend
npm install
npm run dev
```

The frontend starts on `http://localhost:5173`

### 3. Open the application

- Open `http://localhost:5173`
- The frontend talks to `http://localhost:8080` by default
- If you want to change the backend URL, set `VITE_API_BASE_URL` before running the frontend

## Useful Endpoints

- `GET /api/dashboard`
- `GET /api/candidates`
- `POST /api/candidates`
- `GET /api/job-postings`
- `POST /api/job-postings`
- `GET /api/applications`
- `POST /api/applications`
- `PATCH /api/applications/{id}/status`

## Demo Notes

- H2 console is available at `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:candidateflow`
- Username: `sa`
- Password: empty

## Why It Fits Your CV

- Shows `Spring Boot`, `REST API`, `JPA`, `Hibernate`, `DTO`, and layered architecture knowledge
- Aligns with backend internship goals and the technologies in your CV
- Gives you a project that can be explained as a realistic business workflow rather than a toy CRUD app
