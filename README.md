# E-Commerce Platform

A backend e-commerce platform built with **Java 21**, **Spring Boot 3.5**, **PostgreSQL**, and **Maven**. Covers users, addresses, catalog, cart, and order checkout. JWT auth and cross-cutting polish are next.

**Base URL:** `http://localhost:8081/ecommerce/v1`

---

## Implementation Status

| Module | Status |
|--------|--------|
| Users | ✅ Implemented |
| Addresses | ✅ Implemented |
| Categories | ✅ Implemented |
| Products | ✅ Implemented |
| Cart / Cart Items | ✅ Implemented (polish remaining) |
| Orders / Order Items | ✅ Implemented (polish remaining) |
| Authentication (JWT) | ⬜ Not started |
| Exception handling | 🔄 Partial |

For detailed APIs, business rules, and file checklists see:
- [`FEATURE_ROADMAP.md`](FEATURE_ROADMAP.md)
- [`structured-project-plan.docs`](structured-project-plan.docs)

## Tech Stack

### In use

| Layer | Technology |
|-------|------------|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| Persistence | Spring Data JPA, Hibernate |
| Database | PostgreSQL |
| Build | Maven |
| Utilities | Lombok, Jakarta Validation |
| Monitoring | Spring Boot Actuator |

### Planned

- JWT + Spring Security
- Swagger / OpenAPI (springdoc)
- Docker & Docker Compose
- Redis, Kafka, AWS

---

## Features

### Implemented

- **Users** — create, list, get, partial update, activate/deactivate, delete
- **Addresses** — CRUD, list by user, `AddressType` enum (SHIPPING, BILLING, etc.)
- **Categories** — CRUD by ID or name
- **Products** — CRUD, search by name, list by category, category validation
- **Cart** — create cart, add item, update quantity, remove item, view cart with totals
- **Orders** — checkout from cart, list orders, order detail, cancel (`@Transactional`)
- **Stock validation** — on cart and checkout flows
- **Exception handling** — `GlobalExceptionHandler` with domain exceptions
- **Health** — welcome endpoint at `/`

### Coming next

1. Foundation polish (exceptions, DTO consistency, validation gaps)
2. JWT authentication + role-based access (ADMIN / CUSTOMER)
3. Swagger, tests, Docker

---

## Project Structure

```text
src/main/java/com/furqan/ecommerce/
├── controller/     User, Address, Category, Product, Cart, Order, Health
├── service/        User, Address, Category, Product, Cart, Order
├── repository/     IUser … ICartItem, IOrder, IOrderItem
├── entity/         User, Address, Category, Product, Cart, CartItem, Order, OrderItem
├── dto/            user, address, category, product, cart, order, common
├── exception/      GlobalExceptionHandler + domain exceptions
├── configs/        EndpointPrinter, StartupSummaryPrinter
├── enums/          AddressType, OrderStatus, PaymentStatus
└── EcommerceApplication.java
```

---

## Prerequisites

- Java 21+
- Maven 3.9+
- PostgreSQL (local or remote)

---

## Getting Started

### 1. Database

Create the database and run the schema:

```bash
createdb ecommerce
psql -d ecommerce -f schema.sql
```

### 2. Configuration

The application reads credentials from environment variables. Set these before starting it:

```bash
export DB_USERNAME=postgres
export DB_PASSWORD='your-database-password'
export JWT_SECRET='replace-with-a-long-random-secret'
```

`DB_URL` defaults to `jdbc:postgresql://localhost:5432/ecommerce`, and `SERVER_PORT`
defaults to `8081`. Other optional settings are documented in
`src/main/resources/application.yaml`.

### 3. Run

```bash
mvn spring-boot:run
```

App runs at: **http://localhost:8081**

---

## API Overview

> **Note:** Current APIs use verb-in-path style and header-based IDs. RESTful path variables + JWT are planned.

### Users — `/ecommerce/v1/users`

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/all` | List all users |
| `GET` | `/user` | Get user (header: `user_id`) |
| `POST` | `/createUser` | Create user |
| `PATCH` | `/updateUser` | Partial update |
| `PATCH` | `/isActive` | Toggle active status |
| `DELETE` | `/deleteUser` | Delete user (header: `user_id`) |

### Addresses — `/ecommerce/v1/addresses`

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/all` | List all addresses |
| `GET` | `/addressId` | Get by ID (header: `address_id`) |
| `GET` | `/userAddress` | List by user (header: `user_id`) |
| `POST` | `/createAddress` | Create address |
| `PATCH` | `/updateAddress` | Update address |
| `DELETE` | `/deleteAddress` | Delete (header: `address_id`) |

### Categories — `/ecommerce/v1/categories`

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/all` | List categories |
| `GET` | `/categoryId` | Get by ID (header: `category_id`) |
| `GET` | `/getCategory` | Get by name (header: `category_name`) |
| `POST` | `/createCategory` | Create category |
| `PATCH` | `/updateCategory` | Update category |
| `DELETE` | `/deleteCategoryById` | Delete by ID |
| `DELETE` | `/deleteCategoryByName` | Delete by name |

### Products — `/ecommerce/v1/products`

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/all` | List products |
| `GET` | `/productId` | Get by ID (header: `product_id`) |
| `GET` | `/productByName?name=` | Get by name |
| `GET` | `/productsByCategoryId?categoryId=` | List by category |
| `POST` | `/addProduct` | Create product |
| `PATCH` | `/updateProduct` | Update product |
| `DELETE` | `/productId` | Delete (header: `product_id`) |

### Cart — `/ecommerce/v1/cart`

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/createCart` | Create or return cart |
| `POST` | `/addItem` | Add/increment item |
| `GET` | `/getCart` | View cart + totals (header: `user_id`) |
| `PATCH` | `/updateQuantity` | Update item quantity |
| `DELETE` | `/removeItem` | Remove cart line item |

### Orders — `/ecommerce/v1/orders`

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/checkout` | Place order from cart |
| `GET` | `/` | List orders (header: `user_id`) |
| `GET` | `/{orderId}` | Order detail (header: `user_id`) |
| `PATCH` | `/{orderId}/cancel` | Cancel order (header: `user_id`) |

### Health

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/` | Welcome message |

---

## Roadmap

| Phase | Scope | Status |
|-------|-------|--------|
| Foundation | Spring Boot, JPA, PostgreSQL, Users | ✅ Done |
| Catalog | Categories + Products | ✅ Done |
| Shopping | Addresses + Cart | ✅ Done |
| Orders | Checkout, list, detail, cancel | ✅ Done |
| Polish | Exception handling, validation fixes | 🔄 In progress |
| Auth | JWT + Spring Security + roles | ⬜ Next |
| Cross-cutting | Swagger, tests, Docker | ⬜ Planned |
| Advanced | Redis, Kafka, AWS, microservices | ⬜ Future |

---

## Learning Objectives

- Java 21 & Spring Boot 3.x
- REST API design & DTO pattern
- PostgreSQL & JPA/Hibernate
- Domain exceptions & global error handling
- Transactional business logic (cart, checkout)
- Authentication, caching, messaging (planned)

---

## Author

**Furqan Moin**  
Backend Engineer | Java | Spring Boot | PostgreSQL  

GitHub: [furqan-moin](https://github.com/furqan-moin)

---

⭐ If you found this project useful, consider giving it a star.
