# Crewmeister Coding Challenge – Exchange Rate Service

## 📌 Overview

This project is a **Spring Boot microservice** that provides EUR foreign exchange (EUR-FX) rates and currency conversion capabilities.

It consumes exchange rate data from the **Deutsche Bundesbank** and exposes optimized APIs for clients.

The implementation goes beyond the original assignment by incorporating **modern backend best practices**, including caching, resilience, scheduling, and SOLID / clean architecture.
Target was to load historical data all at once during server start and with minimal time not in minutes but just in seconds.

---

## 🎯 Implemented User Stories

* ✅ As a client, I want to get a list of all available currencies
* ✅ As a client, I want to get all EUR-FX exchange rates
* ✅ As a client, I want to get exchange rates for a specific date
* ✅ As a client, I want to convert a foreign currency amount to EUR

---

## 🏗️ Architecture

This project follows a hybrid approach:

### 🔷 Hexagonal Architecture (Ports & Adapters)

* External API integration via ports
* Decoupled business logic

### 🔷 N-Layered Architecture

* Controller → Service → Repository → Entity

### 🔷 Key Design Principles

* SOLID principles
* Clean code practices
* Separation of concerns
* Performance and Scalability
* High testability

---

## ⚙️ Tech Stack

* Java 21
* Spring Boot 3.5.10
* Spring Data JPA
* In-memory H2 Database
* OpenFeign (External API calls + Retry)
* Resilience4j (Circuit Breaker)
* ShedLock (Distributed Scheduler Locking)
* Caffeine Cache (To Reduce Database Hits)
* Swagger / OpenAPI (Documentation)
* Bean Validation
* Docker (dockerfile added)
* Lombok

---

## 🚀 Features & Enhancements

### 📡 External API Integration

* Uses OpenFeign to fetch exchange rates + Retry in case there is any exception
* Resilient calls with Circuit Breaker

### ⚡ Caching (Caffeine)

* Reduces repeated API/database calls
* Improves performance significantly

### ⏱️ Scheduler + ShedLock

* Daily update of exchange rates (aligned with Bundesbank updates)
* Prevents duplicate execution in distributed environments

### 📦 Batch Processing

* Efficient bulk insertion of exchange rates into DB during server start

### 🗄️ JPA Projections

* Fetch only required columns for better performance

### ❗ Exception Handling

* Centralized via `@ControllerAdvice`

### 🧾 Validation

* Request validation using Bean Validation

---

## 🗄️ Database Strategy

The application uses an **H2 in-memory database** for simplicity.

### Initialization Strategy

Instead of querying the external API on every request:

1. Data is fetched once
2. Stored in DB
3. Served from DB for the first time
4. For fast api responses, cache and pagination makes api calls consistent low-latency responses regardless of dataset size 🚀

👉 This improves performance drastically:

* External API: ~4 second (depending on internet speed)
* Database: ~milliseconds
* Server booting + upload historical data takes around ~30 to 50 seconds

---

## 🔄 Scheduler

A scheduled job runs week days to keep the database up to date:

```java
@Scheduled(cron = "0 0 16 ? * MON-FRI", zone = "Europe/Berlin")
public void updateDB() {
    // Fetch latest data and store in DB
}
```

---

## 📘 API Documentation

Swagger UI:

👉 http://localhost:8080/swagger-ui/index.html
![Architecture Diagram](src/main/resources/images/Swagger-UI.png)

---

## 📡 API Endpoints

### 1️⃣ Get All Currencies

```
GET /api/currencies
```

**Query Params:**

* `page`
* `size`
* `sortAsc`
```
curl -X 'GET' \
  'http://localhost:8080/api/currencies?page=0&size=10&sortAsc=false' \
  -H 'accept: */*'
```
---

### 2️⃣ Get All Exchange Rates

```
GET /api/exchange-rate
```

**Query Params:**

* `pageNo`
* `size`
* `sortByDateAsc`
* `ignoreNullRates`
```
curl -X 'GET' \
  'http://localhost:8080/api/exchange-rate?pageNo=0&size=10&sortByDateAsc=true&ignoreNullRates=false' \
  -H 'accept: */*'
```
---

### 3️⃣ Get Exchange Rate by Currency & Date

```
GET /api/exchange-rate/{currencyCode}/{date}
```
```
curl -X 'GET' \
'http://localhost:8080/api/exchange-rate/AUD/1999-01-04' \
-H 'accept: */*'
```
---

### 4️⃣ Get Exchange Rates by Date

```
GET /api/exchange-rate/{date}
```
```
curl -X 'GET' \
  'http://localhost:8080/api/exchange-rate/1999-01-04?ignoreNullRates=false' \
  -H 'accept: */*'
```
---

### 5️⃣ Convert Currency to EUR

```
POST /api/exchange-rate/convert
```

**Request Body:**

```json
{
  "currencyCode": "USD",
  "date": "2026-03-18",
  "exchangeAmount": 100
}
```
```
curl -X 'POST' \
  'http://localhost:8080/api/exchange-rate/convert' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "currencyCode": "USD",
  "date": "2026-03-18",
  "exchangeAmount": 0.1
}'
```
---

## ▶️ Running the Application

### Prerequisites

* Java 21
* Maven 3+

### Run locally

```
mvn clean install
mvn spring-boot:run
```

---

## 🐳 Docker

```
docker build -t exchange-service .
docker run -p 8080:8080 exchange-service
```

---

## 🧪 Testing

* Unit Tests → Mockito + JUnit 5
* Integration Tests → SpringBootTest + H2
* Repository Tests → @DataJpaTest

---

## 📂 Project Structure (Simplified)

```
config           → App configuration(cache, shedlock)
controller/      → REST APIs
service/         → Business logic
repository/      → DB layer
external
  feign          → Feign Declaration
  adapter/       → External API logic
  port/          → Interfaces (Hexagonal)
dto/             → Request/Response models
exception/       → Global error handling
```
---
## 🧩 Architecture Diagram
![Architecture Diagram](src/main/resources/images/architecture.png)
---
## ⚖️ Trade-offs & Design Decisions

- **H2 Database**
  - Chosen for simplicity and fast setup
  - Easily replaceable with PostgreSQL in production

- **Database-first approach**
  - Improves response time significantly
  - Trade-off: chances of data duplication from external API

- **Caffeine Cache**
  - Reduces DB hits and improves latency
  - Trade-off: requires cache invalidation strategy in future

- **Scheduler instead of real-time fetch**
  - Ensures stable and predictable performance
  - Trade-off: slight delay in latest data availability

- **Batch processing at startup**
  - Fast bulk ingestion (~10–20 seconds)
  - Trade-off: increased startup time
---

## 🧠 Key Design Decisions

* Database-first approach for performance
* Resilient external API integration
* Clean architecture for maintainability
* Scalable scheduling with locking
* Optimized queries with projections

---

## 📌 Notes

* H2 database is used for simplicity (can be replaced with PostgreSQL)
* Scheduler ensures data freshness
* Designed to scale in distributed environments

---

## 🔗 References

* Spring Initializer: https://start.spring.io/
* Bundesbank Exchange Rates API:
  https://www.bundesbank.de/en/statistics/exchange-rates

---
