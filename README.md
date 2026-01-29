# 📝 Robust Blog Application REST API

A fully functional, secure, and scalable backend for a Blog Application built with **Java** and **Spring Boot**. This project demonstrates a production-ready REST API architecture featuring Role-Based Access Control (RBAC), JWT Authentication, and comprehensive API documentation via Swagger UI.

## 🚀 Key Features

* **Secure Authentication:** User Registration and Login with **JWT (JSON Web Tokens)**. Includes Access Token and Refresh Token implementation.
* **Role-Based Access:**
    * **Public APIs:** Accessible to everyone (View posts, search, view comments).
    * **Private APIs:** Protected routes requiring a valid JWT (Create/Update/Delete posts and comments).
* **CRUD Operations:** Complete Create, Read, Update, Delete functionality for Users, Posts, and Comments.
* **Search Functionality:** Keyword-based search for blog posts.
* **API Documentation:** Interactive API exploration using **Swagger UI (OpenAPI 3.0)**.
* **Exception Handling:** Global exception handling for clean and meaningful error responses.

---

## 🛠️ Tech Stack

* **Language:** Java
* **Framework:** Spring Boot 3+
* **Database:** MySQL
* **Security:** Spring Security, JWT (Auth)
* **ORM:** Spring Data JPA (Hibernate)
* **API Documentation:** SpringDoc OpenAPI (Swagger UI)
* **Build Tool:** Maven

---

## 🏗️ High Level Design (HLD)

The application follows a standard layered architecture to ensure separation of concerns and maintainability.

```mermaid
graph TD
    Client["Client / Frontend / Swagger"]
    
    subgraph "Spring Boot Application"
        Security["Spring Security Filter Chain (JWT Auth)"]
        Controller["Controllers (API Layer)"]
        Service["Service Layer (Business Logic)"]
        Repo["Repository Layer (Data Access)"]
    end
    
    DB[("MySQL Database")]

    Client -->|HTTP Request| Security
    Security -->|Authenticated| Controller
    Controller --> Service
    Service --> Repo
    Repo -->|SQL Query| DB
erDiagram
    USER ||--o{ POST : writes
    USER ||--o{ COMMENT : writes
    POST ||--o{ COMMENT : has
    
    USER {
        int id
        string username
        string email
        string password
    }
    POST {
        int id
        string title
        string content
        date created_at
    }
    COMMENT {
        int id
        string text
        date created_at
    }
src/main/java/com/Aryan/Blog/Application
├── config          # Security (JWT, SecurityConfig) and Swagger config
├── controller      # REST Controllers (Endpoints)
├── DTOs            # Data Transfer Objects (Request/Response models)
├── entity          # JPA Entities (Database Tables)
├── Exception       # Global Exception Handler and Custom Exceptions
├── filter          # JWT Request Filters
├── repository      # Data Access Layer (JPA Interfaces)
├── service         # Business Logic Layer
└── utility         # Helper classes (JWT Utilities)
Method,Endpoint,Description
POST,/auth/signup,Register a new user
POST,/auth/login,Login and receive JWT Access/Refresh tokens
POST,/auth/Refresh,Generate new Access Token using Refresh Token
Method,Endpoint,Description
GET,/public/post,Get all blog posts
GET,/public/search,Search posts by keyword
GET,/public/user/{userId},View user profile by ID
GET,/public/post/{postId},View specific post details
GET,/public/comment/{postId},View all comments on a post
Method,Endpoint,Description
POST,/create,Create a new blog post
PUT,/update/{postId},Update an existing post
DELETE,/{postId},Delete a post
POST,/comment/{postId},Add a comment to a post
DELETE,/comment/{commentId},Delete a specific comment
