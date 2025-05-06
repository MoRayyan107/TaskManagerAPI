# 🗂️ Task Manager API

## 📌 Project Description
The Task Manager API is a secure, scalable backend service built with **Java + Spring Boot** that helps users manage their tasks with proper **authentication and authorization**.  
This RESTful API supports **CRUD operations**, **task completion toggling**, **task history logging**, and uses **JWT tokens** to protect routes.

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
- Secure login via `api/auth/login` endpoint
- Generates and validates JWT tokens
- Protects all routes except `api/auth/**`, `Swagger UI`, `/login.html` and `/register.html`

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
- 📃 List all tasks (simple list, no pagination)
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
    - "Edit"
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
4. You can use this app in two ways:

### 🔁 Option 1: Using Postman

- **Register**:  
  `POST /api/auth/register`

- **Login**:  
  `POST /api/auth/login`  
  This returns a JWT token.

- For all subsequent requests (e.g., `/api/tasks/**`), add the JWT in **Postman's Authorization tab**:
    - Type: `Bearer Token`
    - Token: `<your JWT token here>`

### 🌐 Option 2: Using Browser (Demo Frontend)

1. **Register** by visiting:  
   `http://localhost:8080/register.html`  
   This will create a new user in the system.

2. **Login** at:  
   `http://localhost:8080/login.html`  
   After logging in successfully, the app will:
    - Generate a JWT
    - Store it in browser memory
    - Redirect you to:  
      `http://localhost:8080/tasks-page.html`

From this page, all task API calls (create, update, delete, mark complete/incomplete) will be authorized using the stored JWT behind the scenes — no Postman or manual headers required.


---

## 🔐 Example Auth Flow (Postman)
1. **Register**
   - POST `/api/auth/register`
   - Body: 
   ```json
   {
     "username": "admin",
     "password": "password",
     "firstName": "John",
     "lastName": "Doe",
     "email": "john@example.com"
   }
2. **Login**
    - POST `/api/auth/login`
    - Body:
   ```json
   {
     "username": "admin",
     "password": "password"
   }
   ```
   - Return Body:
   ```json
   {
     "jwt": "YTjkUY4HJ67......." 
   }
   ```
---
##  🗂️ Project Structure  

````aiignore
TaskManagerAPI/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.manager.TaskManagerAPI/
│   │   │       ├── config/                 # JWT filter, security configs, Swagger setup
│   │   │       ├── constants/              # App-wide constants (e.g., static messages)
│   │   │       ├── controller/             # REST controllers (Auth, Task, Frontend)
│   │   │       ├── ErrorMessages/          # Custom error messages & global exception handler
│   │   │       ├── model/                  # Entity classes (User, Task, Request/Response DTOs)
│   │   │       ├── repository/             # Spring Data JPA Repositories
│   │   │       ├── services/               # Business logic (Auth, Task, UserDetailService)
│   │   │       └── Util/                   # JWT utility helper
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/                    # style.css
│   │       │   └── js/                     # login.js, task.js
│   │       ├── login.html
│   │       ├── register.html
│   │       └── task.html
│   └── test/
│       └── java/
│           └── com.manager.TaskManagerAPI/
│               ├── controller/            # Unit tests for Auth & Task Controllers
│               ├── ErrorMessages/         # Tests for custom error logic
│               ├── model/                 # Entity-related unit tests
│               ├── services/              # Service-level tests (Task, Auth)
│               └── Util/                  # JWT utility tests
│
├── application.properties                 # App config
├── pom.xml                                # Project dependencies
├── README.md                              # Project overview & setup instructions
├── LICENSE, .gitignore, .gitattributes    # Standard project files
├── TaskManager API.postman_collection.json # Postman collection for API testing
├── LayoutGrid.png                         # Optional: layout wireframe

````

---

## 🙌 Credits
Developed by **Mohammad Rayyan**  
🧠 **Backend Developer** — focused on designing the Spring Boot API, secure architecture, and system logic.  
🎨 The frontend was scaffolded using **GenAI tools** and kept minimal, primarily for demonstration purposes.