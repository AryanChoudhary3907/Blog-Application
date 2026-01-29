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