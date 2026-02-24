# Architecture Overview

## Stack
- **Language**: Java 25
- **Framework**: Spring Boot 3.5.0
- **View**: Thymeleaf
- **Persistence**: Spring Data JPA + Hibernate
- **DB**: PostgreSQL (prod), H2 (test)
- **Monitoring**: Actuator + Micrometer (Prometheus)
- **Cache**: Spring Cache (ConcurrentMap)

## Layers
```
Controller (REST/UI)
  ↓
Service (Transactional, caching)
  ↓
Repository (Spring Data JPA)
  ↓
Database (PostgreSQL/H2)
```

## Key Modules
- `controller` — REST endpoints (`/products` etc.)
- `service` — Business logic, caching (`ProductService`)
- `repository` — JPA repositories (`ProductRepository` et al.)
- `config` — `CacheConfig`, `MetricsConfig`
- `dto` — Request/Response DTOs
- `exception` — Global exception handlers

## Cross-Cutting
- **Caching**: `@EnableCaching`, cache `products`
- **Metrics**: `PrometheusMeterRegistry`, `/actuator/prometheus`
- **Health**: `/actuator/health`
- **Logging**: SLF4J (Logback default)

## Runtime Ports
- **App**: 8080
- **Actuator**: `/actuator/**`

## Data Model
- Entities cover entire domain (users, orders, products, etc.) under `model.entity.*`.
- `Product` uses `@PrePersist`/`@PreUpdate` to set timestamps.

## Diagrams (Ascii)
```
+----------------------+
|      Web Client      |
+----------+-----------+
           |
           v
+----------------------+
|   Controller (@Rest) |
+----------+-----------+
           |
           v
+----------------------+
|   Service (@Service) |
| - caching            |
+----------+-----------+
           |
           v
+----------------------+
| Repository (JPA)     |
+----------+-----------+
           |
           v
+----------------------+
|  PostgreSQL / H2     |
+----------------------+
```

## External Interfaces
- **REST**: `/products` CRUD
- **Metrics**: `/actuator/prometheus`
- **OpenAPI**: `/v3/api-docs`, `/swagger-ui.html` (Springdoc)

## Security Notes
- No auth configured yet (to be addressed in later phases if required)

## Deployment
- Jar (Spring Boot)
- Docker (see Dockerfile)

## Observability
- Actuator endpoints exposed (health/info/metrics/prometheus)
- Prometheus scrape target ready
