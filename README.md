# 🗂️ Task Manager API

## 📌 Project Description
The Task Manager API is a secure, scalable backend service built with **Java + Spring Boot** that helps users manage their tasks with proper **authentication and authorization**.  
This RESTful API supports **CRUD operations**, **pagination**, **task history logging**, and uses **JWT tokens** to protect routes.

---

## 🎯 Goals
- Learn real-world backend development using Spring Boot
- Understand and apply JWT authentication
- Build and test secure REST APIs
- Practice Spring Security, unit testing, and clean architecture
- Add a professional project to your GitHub portfolio

---

## 🔐 Authentication
- Uses Spring Security 6+
- Secure login via `/authenticate` endpoint
- Generates and validates JWT tokens
- Protects all routes except `/authenticate`

🧾 Add your token to headers:  
`Authorization: Bearer <your-token-here>`

---

## 🔧 Tech Stack

| Layer            | Technology             |
|------------------|------------------------|
| Language         | Java (17+)             |
| Framework        | Spring Boot            |
| Database         | H2 (in-memory)         |
| Auth & Security  | Spring Security + JWT  |
| Build Tool       | Maven                  |
| API Testing      | Postman / curl         |
| IDE              | IntelliJ / VS Code     |

---

## 🚀 API Features
- ✅ User Login + JWT Token
- ✅ Create a task
- ✅ Update a task (with task history tracking)
- 📃 List all tasks (with pagination & sorting)
- ✅ Mark a task as completed
- ❌ Delete a task
- 📜 View task history logs by task ID

---

## 📦 Getting Started
1. Clone the repository
2. Open in IntelliJ or your preferred IDE
3. Run `TaskManagerApiApplication.java`
4. Use Postman to:
    - Send login request to `/authenticate`
    - Use the returned JWT to access `/tasks`

---

## 🔐 Example Auth Flow (Postman)
1. **Login**
    - POST `/authenticate`
    - Body:
   ```json
   {
     "username": "admin",
     "password": "password"
   }
