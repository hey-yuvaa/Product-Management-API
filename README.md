# 🚀 Enterprise Product Management API

An **enterprise-grade, high-performance RESTful API** built with **Spring Boot 3.3.0, Spring Data JPA, Spring Security, and MySQL**.

The application is designed around clean layered architecture, stateless JWT authentication, database-backed refresh-token rotation, JPA auditing, standardized API responses, pagination, and modern interactive API documentation through Scalar.

---

## ✨ Key Highlights

- 🔐 **Stateless JWT Authentication**
- 🔄 **Database-backed Refresh Token Rotation**
- 🛡️ **Spring Security with Role-Based Authorization**
- 📝 **Automatic JPA Auditing**
- 🧱 **Clean Layered Architecture**
- 📦 **DTO-based Request/Response Design**
- 📄 **Standardized API Response Envelopes**
- ⚡ **Optimized JPA Relationships & Database Indexing**
- 📊 **Clean Pagination Responses**
- 🗄️ **MySQL 8.x Database**
- 📚 **OpenAPI 3.1 Documentation**
- 🌑 **Modern Scalar API Reference UI**
- ☕ **Pure Java Architecture using Java 17 Records**
- 🧪 **Maven-based Build & Dependency Management**

---

# 🏗️ Architecture

The application follows a clean **Controller → Service → Repository** architecture.

```text
                    ┌───────────────────────┐
                    │     HTTP Client       │
                    │     Scalar UI         │
                    └───────────┬───────────┘
                                │
                         Bearer JWT
                                │
                                ▼
                    ┌───────────────────────┐
                    │  SecurityFilterChain  │
                    └───────────┬───────────┘
                                │
                                ▼
                 ┌─────────────────────────────┐
                 │ JwtAuthenticationFilter     │
                 │                             │
                 │ • Extract JWT               │
                 │ • Validate Token            │
                 │ • Authenticate Principal    │
                 └──────────────┬──────────────┘
                                │
                                ▼
                    ┌───────────────────────┐
                    │   Controller Layer    │
                    │                       │
                    │ • Request Validation  │
                    │ • DTO Mapping         │
                    │ • HTTP Responses      │
                    └───────────┬───────────┘
                                │
                                ▼
                    ┌───────────────────────┐
                    │     Service Layer     │
                    │                       │
                    │ • Business Logic      │
                    │ • Transactions        │
                    │ • Security Rules      │
                    └───────────┬───────────┘
                                │
                                ▼
                    ┌───────────────────────┐
                    │   Repository Layer    │
                    │                       │
                    │ • Spring Data JPA     │
                    │ • Database Queries    │
                    └───────────┬───────────┘
                                │
                                ▼
                    ┌───────────────────────┐
                    │       MySQL 8.x       │
                    └───────────────────────┘
```

---

# 🔐 Authentication & Security

The API implements **stateless JWT authentication** using Spring Security.

### Authentication Flow

```text
Login
  │
  ▼
Username + Password
  │
  ▼
AuthenticationManager
  │
  ▼
Credentials Validated
  │
  ├───────────────┐
  ▼               ▼
Access Token   Refresh Token
15 Minutes     7 Days
                  │
                  ▼
              MySQL DB
```

### Token Strategy

| Token | Lifetime | Storage | Purpose |
|---|---:|---|---|
| Access Token | 15 minutes | Client | API authentication |
| Refresh Token | 7 days | Database | Access-token renewal |

Refresh tokens are persisted in the database and follow **single-use revocation/rotation rules**, reducing the risk associated with long-lived credentials.

### Protected Requests

```http
Authorization: Bearer <access-token>
```

All product-management endpoints require authentication.

---

# 👤 Role-Based Authorization

Product endpoints support role-based access using Spring Security.

```text
ROLE_USER
ROLE_ADMIN
```

Protected routes require an authenticated user with the appropriate role.

---

# 📝 JPA Auditing

The application automatically tracks entity creation and modification history using a reusable:

```text
BaseAuditableEntity
```

Auditing fields include:

```text
createdBy
createdOn
modifiedBy
modifiedOn
```

Example:

```json
{
  "id": 1,
  "productName": "Ultra-Wide Monitor 34-Inch",
  "createdBy": "admin",
  "createdOn": "2026-09-04T10:45:00",
  "modifiedBy": "admin",
  "modifiedOn": "2026-09-04T10:45:00"
}
```

The authenticated principal is used as the auditing user.

---

# 🗄️ Persistence Design

The persistence layer uses:

- Spring Data JPA
- Hibernate
- MySQL 8.x
- Strict relational mappings
- Foreign-key indexing
- Bidirectional parent-child relationship management

### Product → Items

```text
Product
   │
   │  OneToMany
   ▼
ProductItem
```

A product can contain multiple inventory items.

```text
Product
 ├── Item 1
 ├── Item 2
 └── Item 3
```

Parent-child lifecycle management is handled through the JPA relationship configuration.

---

# 📦 Standardized API Responses

The API follows a consistent response structure using:

```text
ApiResponse<T>
```

Example:

```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "48b64e03-bc97-49ec-8e7d-94bbadad92e3",
    "tokenType": "Bearer"
  }
}
```

This keeps API responses predictable for frontend clients and other consumers.

---

# 📄 Pagination

Product listings use a dedicated:

```text
PageResponse<T>
```

pagination envelope instead of exposing Spring's internal `Page` structure directly.

Example:

```json
{
  "success": true,
  "message": "Products retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "productName": "Ultra-Wide Monitor 34-Inch"
      }
    ],
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 1,
    "totalPages": 1,
    "last": true
  }
}
```

### Pagination Fields

| Field | Description |
|---|---|
| `content` | Current page records |
| `pageNumber` | Current page index |
| `pageSize` | Number of records per page |
| `totalElements` | Total number of records |
| `totalPages` | Total number of pages |
| `last` | Indicates whether this is the final page |

---

# 📚 API Documentation

The project provides interactive API documentation using:

- SpringDoc OpenAPI 3.1
- Scalar API Reference

After starting the application, open:

```text
http://localhost:8081/scalar
```

The Scalar interface provides:

- OpenAPI 3.1 API documentation
- Interactive endpoint testing
- Bearer-token authentication
- Request/response inspection
- Native request code generation

Supported code-generation formats include:

```text
cURL
Node.js
Python
PHP
Ruby
```

---

# 🔗 API Endpoints

## 🔑 Authentication

Base URL:

```text
/api/v1/auth
```

| Method | Endpoint | Description | Authentication |
|---|---|---|---|
| `POST` | `/register` | Register a new user | ❌ |
| `POST` | `/login` | Authenticate and obtain JWT pair | ❌ |
| `POST` | `/refresh-token` | Rotate refresh token and obtain new access token | ❌ |

### Login Request

```json
{
  "username": "admin",
  "password": "password123"
}
```

### Login Response

```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "48b64e03-bc97-49ec-8e7d-94bbadad92e3",
    "tokenType": "Bearer"
  }
}
```

---

# 📦 Product Management

Base URL:

```text
/api/v1/products
```

| Method | Endpoint | Description | Authentication |
|---|---|---|---|
| `POST` | `/` | Create product with items | 🔒 Required |
| `GET` | `/` | Get paginated products | 🔒 Required |
| `GET` | `/{id}` | Get product by ID | 🔒 Required |
| `PUT` | `/{id}` | Update product | 🔒 Required |
| `DELETE` | `/{id}` | Delete product and associated items | 🔒 Required |
| `GET` | `/{id}/items` | Get inventory items for a product | 🔒 Required |

---

## ➕ Create Product

### Request

```json
{
  "productName": "Ultra-Wide Monitor 34-Inch",
  "items": [
    {
      "quantity": 10
    },
    {
      "quantity": 25
    }
  ]
}
```

### Result

The product and its associated inventory items are persisted through the configured JPA relationship.

---

# 🛡️ Security Configuration

### Public Endpoints

```text
/api/v1/auth/**
/scalar
/scalar.html
/v3/api-docs/**
```

### Protected Endpoints

```text
/api/v1/products/**
```

Every protected request must provide:

```http
Authorization: Bearer <JWT>
```

---

# ⚙️ Technology Stack

| Technology | Version / Purpose |
|---|---|
| **Java** | 17 |
| **Spring Boot** | 3.3.0 |
| **Spring Security** | Authentication & Authorization |
| **Spring Data JPA** | Persistence |
| **Hibernate** | ORM |
| **MySQL** | 8.x |
| **JJWT** | 0.11.5 |
| **SpringDoc OpenAPI** | 3.1.0 |
| **Scalar** | API Documentation |
| **Apache Maven** | Build Tool |

---

# 🧠 Architectural Principles

### Pure Java Architecture

The application avoids unnecessary bytecode-manipulation and compiler-dependent libraries.

Modern Java 17 features such as **records** are used for immutable DTO-style data structures where appropriate.

### Separation of Concerns

Responsibilities are separated across layers:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Each layer has a focused responsibility, improving maintainability and testability.

### DTO-Based API Design

Request and response DTOs prevent persistence entities from becoming the public API contract.

### Transactional Business Operations

Business operations that modify related entities are handled within appropriate transactional boundaries.

---

# 🚀 Getting Started

## Prerequisites

Make sure the following are installed:

- JDK 17+
- MySQL 8.x
- Maven 3.8+
- Git

You can also use the included Maven Wrapper:

```text
./mvnw
```

---

## 1. Clone the Repository

```bash
git clone <your-repository-url>
cd <project-directory>
```

---

## 2. Create the Database

Create the MySQL database:

```sql
CREATE DATABASE product_db;
```

---

## 3. Configure Database Connection

Update:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/product_db?createDatabaseIfNotExist=true&useSSL=false
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update

server.port=8081
```

> ⚠️ Do not commit real database passwords, JWT secrets, or other credentials to source control. Use environment variables or externalized configuration for production deployments.

---

# ▶️ Run the Application

### Build

```bash
./mvnw clean package -DskipTests
```

### Start

```bash
./mvnw spring-boot:run
```

The API will start on:

```text
http://localhost:8081
```

---

# 📖 Open API Documentation

Once the application is running:

```text
http://localhost:8081/scalar
```

Use the **Authorize** option in Scalar to provide your JWT:

```text
Bearer <access-token>
```

You can then test protected product endpoints directly from the documentation interface.

---

# 🔄 Complete Request Flow

A typical authenticated product request follows this flow:

```text
Client
  │
  │ POST /api/v1/products
  │ Authorization: Bearer JWT
  ▼
SecurityFilterChain
  │
  ▼
JwtAuthenticationFilter
  │
  ├── Extract JWT
  ├── Validate JWT
  └── Set Authentication
  │
  ▼
ProductController
  │
  ├── Validate Request DTO
  └── Call Service
  │
  ▼
ProductService
  │
  ├── Execute Business Logic
  ├── Manage Product + Items
  └── Transaction Boundary
  │
  ▼
ProductRepository
  │
  ▼
Hibernate / JPA
  │
  ▼
MySQL
  │
  ▼
Response DTO
  │
  ▼
ApiResponse<T>
  │
  ▼
HTTP Client
```

---

# 🔑 Refresh Token Flow

```text
Access Token Expired
        │
        ▼
Client sends Refresh Token
        │
        ▼
Refresh Token validated
        │
        ▼
Database lookup
        │
        ▼
Existing token revoked
        │
        ▼
New Access Token
        │
        ▼
New Refresh Token
        │
        ▼
Client continues API requests
```

This allows the client to obtain a new short-lived access token without requiring the user to log in again.

---

# 📁 Project Structure

A typical project structure follows the layered architecture:

```text
src/
└── main/
    ├── java/
    │   └── com.example.product/
    │       ├── config/
    │       ├── controller/
    │       ├── dto/
    │       ├── entity/
    │       ├── exception/
    │       ├── repository/
    │       ├── security/
    │       └── service/
    │
    └── resources/
        └── application.properties
```

> Package names may differ depending on the project's actual base package.

---

# 📌 Future Improvements

Potential production-level extensions include:

- Unit and integration testing
- Testcontainers-based database testing
- Dockerized deployment
- CI/CD pipeline
- Centralized exception/error codes
- Structured application logging
- Redis-based caching
- Rate limiting
- Observability with metrics and tracing
- Production secrets management
- Cloud deployment

---

# 👨‍💻 Engineering Focus

This project demonstrates practical backend engineering concepts including:

```text
Java 17
   ↓
Spring Boot
   ↓
REST API Design
   ↓
DTO Architecture
   ↓
Spring Security
   ↓
JWT Authentication
   ↓
Refresh Token Rotation
   ↓
JPA / Hibernate
   ↓
MySQL
   ↓
Transactions
   ↓
Auditing
   ↓
Pagination
   ↓
OpenAPI / Scalar
```

The goal is to provide a maintainable backend foundation suitable for enterprise-style application development.

---

## ⭐ If You Found This Project Useful

Feel free to star ⭐ the repository and explore the implementation.
