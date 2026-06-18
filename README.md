# E-Commerce Platform

A production-oriented E-Commerce Platform backend built using Java 21, Spring Boot 3.5, PostgreSQL, Docker, and Maven.

The goal of this project is to gain hands-on experience with modern backend development, scalable system design, REST APIs, database management, caching, messaging systems, and cloud-native application development.

---

## Tech Stack

### Backend

* Java 21
* Spring Boot 3.5
* Spring Data JPA
* Hibernate ORM
* Maven

### Database

* PostgreSQL 17

### Monitoring

* Spring Boot Actuator

### DevOps

* Docker
* Docker Compose

### Version Control

* Git
* GitHub

### Planned Integrations

* Redis
* Apache Kafka
* AWS
* JWT Authentication
* Swagger / OpenAPI

---

## Features

### Current Features

* Spring Boot application setup
* PostgreSQL integration
* Dockerized PostgreSQL database
* User entity implementation
* User CRUD APIs
* Spring Data JPA integration
* Actuator monitoring endpoints
* Production-style startup summary
* Layered architecture implementation
* Exception-ready backend foundation

### Planned Features

#### User Management

* User registration
* User profile management
* User activation/deactivation

#### Product Management

* Product CRUD operations
* Product search
* Product filtering and sorting

#### Category Management

* Category CRUD operations
* Hierarchical category support

#### Shopping Cart

* Add to cart
* Remove from cart
* Update cart quantity

#### Order Management

* Place order
* Order history
* Order tracking

#### Authentication & Authorization

* JWT Authentication
* Role-based access control

#### Payment Integration

* Payment gateway integration
* Transaction tracking

#### Inventory Management

* Inventory tracking
* Stock management

#### Performance & Scalability

* Redis caching
* Kafka event-driven architecture
* Database optimization
* API rate limiting

---

## Project Structure

```text
src/main/java/com/furqan/ecommerce
│
├── configs
│   └── StartupSummaryPrinter.java
│
├── controller
│   ├── HealthController.java
│   └── UserController.java
│
├── dto
│
├── entity
│   └── UserEntity.java
│
├── repository
│   └── UserRepository.java
│
├── service
│   └── UserService.java
│
└── EcommerceApplication.java
```

---

## Prerequisites

* Java 21+
* Maven 3.9+
* Docker Desktop
* PostgreSQL 17+

---

## Application URL

```text
http://localhost:8081
```
---

### User APIs

#### Get All Users

```http
GET /ecommerce/v1/users/all
```

#### Get User By ID

```http
GET /ecommerce/v1/users/user?userId=1
```

#### Create User

```http
POST /ecommerce/v1/users/createUser
```

#### Update User Status

```http
PATCH /ecommerce/v1/users/isActive
```

#### Delete User

```http
DELETE /ecommerce/v1/users/deleteUser
```

---

## Learning Objectives

This project is being developed to strengthen practical knowledge of:

* Java 21
* Spring Boot 3.x
* REST API Development
* PostgreSQL
* JPA & Hibernate
* Docker
* Database Design
* Backend Engineering Best Practices
* Scalable System Design
* Microservices Architecture
* Distributed Systems
* Cloud-Native Development

---

## Roadmap

### Phase 1 (Completed)

* Spring Boot Setup
* PostgreSQL Integration
* Docker Setup
* User APIs
* Actuator Integration

### Phase 2 (In Progress)

* Product APIs
* Category APIs
* Validation Framework
* Global Exception Handling

### Phase 3

* JWT Authentication
* Redis Integration
* Kafka Integration

### Phase 4

* Docker Compose
* CI/CD Pipeline
* AWS Deployment

---

## Author

**Furqan Moin**

Backend Engineer | Java | Spring Boot | PostgreSQL

GitHub: https://github.com/furqan-moin

---

⭐ If you found this project useful, consider giving it a star.
