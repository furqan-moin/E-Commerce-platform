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
- [`docs/security-module-design.md`](docs/security-module-design.md) — proposed authentication, authorization, token, and Amazon federation design

---

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

- Java 21 (Amazon Corretto 21 recommended)
- Docker, if running PostgreSQL locally in a container
- PostgreSQL with the `ecommerce` database created
- Git

The project includes the Maven Wrapper, configured to download and use Maven 3.9.11. A separate Maven installation is not required.

## Getting Started

### 1. Clone the repository

```bash
git clone "https://github.com/furqan-moin/E-Commerce-platform.git"
cd E-Commerce-platform
```

### 2. Start PostgreSQL

Ensure PostgreSQL is running and accessible at `localhost:5432`, and that the `ecommerce` database exists.

If you use the existing local Docker container, start it if necessary and verify that PostgreSQL is accepting connections.

### 3. Configure environment variables

The application reads its configuration from `src/main/resources/application.yaml`.

For a local development session, export the required variables in your terminal:

```bash
export DB_URL="jdbc:postgresql://localhost:5432/ecommerce"
export DB_USERNAME="postgres"
export DB_PASSWORD="<your-local-database-password>"
export JWT_SECRET="$(openssl rand -base64 32)"
```

Replace the database username and password with your actual local PostgreSQL credentials. Keep secrets out of source control.

The application also supports optional configuration variables:

- `SERVER_PORT` — defaults to `8081`
- `JPA_DDL_AUTO` — defaults to `update`
- `JPA_SHOW_SQL` — defaults to `false`
- `JWT_EXPIRATION_MS` — defaults to `900000` (15 minutes)
- `JWT_REFRESH_EXPIRATION_MS` — defaults to `604800000` (7 days)
- `MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE` — defaults to `health,info`

These defaults are intended for local development and should be reviewed before production deployment.

### 4. Run the application

```bash
./mvnw spring-boot:run
```
Base URL: `http://localhost:8081/ecommerce/v1`

### 5. Verify application health

In another terminal, run:

```bash
curl -i http://localhost:8081/actuator/health
```

Expected response:

```json
{"status":"UP"}
```


### 6. Build and verify

```bash
./mvnw clean verify
```

This command performs a clean Maven build, compiles the application, runs any discovered tests, and packages the executable Spring Boot JAR.

The generated artifact is located at:

`target/ecommerce-platform-0.0.1-SNAPSHOT.jar`

### Technology Versions

- Java: 21 (verified locally with Amazon Corretto 21.0.11)
- Spring Boot: 3.5.0
- Maven Wrapper: 3.3.4
- Maven distribution: 3.9.11
- Database: PostgreSQL (local development currently uses PostgreSQL 17.10)

---

## Getting Started

### 1. Database

Create the database and run the schema:

```bash
createdb ecommerce
psql -d ecommerce -f schema.sql
```

### 2. Configuration

Update `src/main/resources/application.properties` if needed:

```properties
server.port=8081
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce
spring.datasource.username=postgres
spring.datasource.password=postgres
```

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
