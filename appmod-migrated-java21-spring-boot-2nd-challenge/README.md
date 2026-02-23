<!-- markdownlint-disable MD013 -->

# SkiShop Monolith (Struts 1.x → Spring Boot) — Docker Compose Quickstart

> Spring Boot 3.5 + PostgreSQL 15 (Java 25). Legacy Struts 1.x roots kept for reference (see `UPGRADE_PLAN.md`).

## ✅ Prerequisites

- Docker (Compose plugin)
- Docker Desktop (Apple Silicon compatible)

## 🚀 Quickstart

```bash
docker compose up -d --build
```

- `app`: Spring Boot (JDK 25, embedded Tomcat 10.1), port `8080`
- `db`: PostgreSQL 15, init SQL: `src/main/resources/db`

## 🔍 Verify

```bash
docker compose ps
docker compose logs -f app
open http://localhost:8080/
# or: curl -I http://localhost:8080/
```

Endpoints:

- `GET /` (Thymeleaf)
- `GET /products`
- `GET /swagger-ui.html`
- `GET /actuator/health`

## 🛑 Stop & Cleanup

```bash
docker compose down
```

Remove volumes:

```bash
docker compose down -v
```

## 🔧 Troubleshooting

- **8080 port conflict**: run app container manually on 18080

```bash
docker compose up -d db
docker run --rm --name skishop-app-boot2-alt \
  --network appmod-migrated-java21-spring-boot-2nd-challenge_default -p 18080:8080 \
  -e DB_HOST=db -e DB_PORT=5432 -e DB_NAME=skishop \
  -e DB_USER=skishop -e DB_PASSWORD=skishop \
  appmod-migrated-java21-spring-boot-2nd-challenge-app
```

- **Apple Silicon**: `export DOCKER_DEFAULT_PLATFORM=linux/amd64` or `docker compose build --platform linux/amd64`.
- **Slow first start**: wait for Postgres initialization (schema/data).

## 🧱 Project Structure

```text
.
├── docker-compose.yml       # docker compose configuration
├── Dockerfile               # multi-stage (build + runtime)
├── pom.xml                  # Maven build (Spring Boot 3.5)
├── src/main/java            # Spring Boot Java sources
├── src/main/resources/db    # Postgres init SQL (01-schema.sql, 02-data.sql)
├── src/main/resources/templates  # Thymeleaf templates
├── docs/                    # operations.md, architecture.md, legacy.md
└── UPGRADE_PLAN.md          # Migration plan notes
```

## 🧪 Local Maven Build (optional)

> JDK 25 / Maven Wrapper included (`./mvnw`).

```bash
./mvnw -B package -DskipTests
```

JAR: `target/*.jar`

## 📌 Notes

- Docker image uses Temurin 21 + Spring Boot (embedded Tomcat 10.1).
- Intentional bugs/vulnerabilities are present for workshops. Do not fix with Code Review/CodeQL.

## 🌱 Spring Boot Prototype

Experimentally added Spring Boot 3.5.x (Java 25) project to `spring-boot-app/`:

```bash
./mvnw spring-boot:run
```

Template: `src/main/resources/templates/index.html`

### Monitoring / Docs

- Actuator: `http://localhost:8080/actuator`
  - Health: `/actuator/health`
  - Metrics: `/actuator/metrics`
  - Prometheus: `/actuator/prometheus`
- OpenAPI: `/v3/api-docs`, `/swagger-ui.html`
- Docs: [`docs/operations.md`](docs/operations.md), [`docs/architecture.md`](docs/architecture.md)

### Phase 2 (Full JPA Coverage)

- **All tables** entity/repository/service implementation complete
- No Lombok (explicit getter/setter, public no-arg ctor)
- Tests: `./mvnw -B test` ✅ (2026-01-22 02:28 JST)
- Note: JPA entities do not adopt Record (due to lifecycle/mutable fields)

### Phase 3 (REST API & DTO & Thymeleaf Framework)

- REST controller/DTO/exception handler implementation (product/cart/order/point/user/address/coupon/return/Admin systems)
- WebMvcTest: Green configuration with mocked repositories and injected service implementation
- Thymeleaf: Added framework for `layout/main`, `fragments/header|footer`, `products/list`, `cart/detail`, `orders/detail`, `admin/*`
- Tests: `./mvnw -B test` ✅ (2026-01-22 02:28 JST)

### Phase 4 (Thymeleaf Full Implementation: JSP→Thymeleaf)

- **Complete**: UI controller (`ViewController`, `AdminViewController`), all screen templates implementation, header/footer/styles, message resource organization, view test addition
- Tests: `./mvnw -B test` ✅ (2026-01-22 03:04 JST)

### Phase 5-8 Complete

- Phase5: Modern Java refactoring complete
- Phase6: REST/UI exception handler & error pages
- Phase7: Enhanced testing (UI/REST exceptions & integration)
- Phase8: Monitoring/caching (Actuator+Prometheus, Spring Cache, HTTP cache performance verification)

#### Tests

```bash
./mvnw -B test
```
- Success: 2026-01-22 15:19 JST

### Phase 9-10
- Phase9: Docs/operations (OpenAPI, operations/architecture)
- **Phase10: Legacy Struts cleanup** — No Struts references in Boot, archival policy documented in `docs/legacy.md`. Root Struts app remains current (for workshop purposes).
