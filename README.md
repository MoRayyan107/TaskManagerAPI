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
- ✅ Mark a task as completed or incomplete
- ❌ Delete a task
- 📜 View task history logs by task ID

---

## 🎨 Frontend UI
The frontend is built using plain **HTML5 + CSS3**, served from the `/static` directory. The UI now includes:

- 🧱 Grid layout: max 3 task cards per row
- ✅ Each task is shown in a **card box** with:
  - Title (heading)
  - Description
  - Priority badge
  - Color-coded bottom bar for LOW / MEDIUM / HIGH
- 🧰 Actions:
  - “Mark Complete” / “Mark Incomplete”
  - “Delete”
- 🎨 Color Legend:
  - 🔴 High = Red  
  - 🟠 Medium = Orange  
  - 🟢 Low = Green
- 🌀 Auto-refresh after every update without scrolling to top

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
   ```

2. **Authorized Request**
    - Add header: `Authorization: Bearer <jwt_token>`
    - Access `/tasks`, `/tasks/create`, etc.

---

## ✨ Upcoming Features
- [ ] Add inline **Edit** button per task (top-right of card)
- [ ] Add pagination (limit 6–9 tasks per page)
- [ ] Task history modal or viewer section
- [ ] Toast/success messages for actions
- [ ] Filter caching to reduce reload hits
- [ ] Dockerize + Deploy-ready packaging

---

## 📂 Project Status
🟢 Actively being developed  
📅 Last updated: 2025-05-02

---

## 🙌 Credits
Developed by **Mohammad Rayyan**