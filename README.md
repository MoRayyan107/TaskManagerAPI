# 🗂️ Task Manager API

## 📌 Project Description
A secure, modular **Java + Spring Boot** backend API to manage tasks with full **JWT-based authentication**,
role-based route protection, and a dynamic frontend using **Mustache (Handlebars)**.

Supports full **CRUD operations**, **pagination**, **task history logging**, and clean Spring Security integration.

---

## 🎯 Goals
- Learn real-world backend development using Spring Boot
- Implement and test secure JWT authentication
- Build REST APIs with best practices (validation, pagination, error handling)
- Write clean, maintainable code with SLF4J logging
- Add a professional full-stack backend project to your portfolio

---

## 🔐 Authentication
- Spring Security 6+
- Login via `/authenticate`
- Generates and validates JWT tokens
- Secures all routes except `/login`, `/`, and `/authenticate`
- Custom `JwtRequestFilter` handles every request

🧾 **Add token in header:**
```
Authorization: Bearer <your-token-here>
```

---

## 🔧 Tech Stack

| Layer            | Technology             |
|------------------|------------------------|
| Language         | Java (17+ / 21)        |
| Framework        | Spring Boot 3.4.4      |
| Database         | H2 (in-memory)         |
| Auth & Security  | Spring Security + JWT  |
| Frontend View    | Mustache (HBS style)   |
| Styling          | HTML5 + CSS3           |
| Testing          | JUnit + MockMvc        |
| API Testing      | Postman                |
| Build Tool       | Maven                  |

---

## 🚀 API Features
- ✅ User Login + JWT Token
- ✅ Create a task (via frontend or API)
- ✅ Update a task (with task history logging)
- ✅ List all tasks (pagination + sorting)
- ✅ Mark task as completed
- ✅ Delete a task
- 📜 View task history logs by task ID
- ✅ Styled Mustache frontend for task list & management

---

## 🧪 Testing Status
> ⚠️ Unit tests for `TaskController` are written but commented pending cleanup for Spring Boot 3.4 filter handling  
> ✅ `@MockBean` approach is being refactored and re-integrated for compatibility

---

## 📦 Getting Started
1. Clone the repository
2. Open in IntelliJ / VS Code
3. Run `TaskManagerApiApplication.java`
4. Access frontend at: [http://localhost:8080/task-page](http://localhost:8080/task-page)
5. OR use Postman:

---

## 📬 Using Postman

### 🔐 1. Authenticate to Get JWT

**Endpoint:**
```
POST /authenticate
```

**Body (raw, JSON):**
```json
{
  "username": "admin",
  "password": "password"
}
```

**Headers:**
```
Content-Type: application/json
```

**Response Example:**
```json
{
  "jwt": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### 🔑 2. Add Token to Headers
```
Authorization: Bearer <your_token_here>
```

### 🔧 3. Example: Create a Task

**POST** `/tasks/create`  
**Headers:**
```
Authorization: Bearer <your_token>
Content-Type: application/json
```

**Body:**
```json
{
  "title": "Test Postman",
  "description": "Making sure Postman works",
  "priority": "HIGH",
  "completed": false
}
```

### 📥 4. Get All Tasks
**GET** `/tasks`  
**Headers:**
```
Authorization: Bearer <your_token>
```

### 🔄 5. Update / Delete / Search / History
- Update: `POST /tasks/update/{id}`
- Delete: `DELETE /tasks/delete/{id}`
- Search: `GET /tasks/search?title=task`
- History: `GET /tasks/history/{id}`

---

## ✨ Future Plans
- [ ] Add Swagger UI for API documentation
- [ ] Finalize controller test coverage
- [ ] Add user registration & DB-backed auth
- [ ] Dockerize the project