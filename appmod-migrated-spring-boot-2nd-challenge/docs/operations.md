# Operations Guide

## Runtime

- **Build**: `./mvnw -f spring-boot-app/pom.xml clean package`
- **Run (dev)**: `./mvnw -f spring-boot-app/pom.xml spring-boot:run`
- **Run (jar)**: `java -jar spring-boot-app/target/skishop-app-2.0.0.jar`
- **Port**: `8080` (Spring Boot)
- **Profiles**: `test` (H2), default (PostgreSQL)

## Configuration

- `spring-boot-app/src/main/resources/application.yml`
- `spring-boot-app/src/test/resources/application-test.yml`
- Env overrides: `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME` (or `DB_USER`), `DB_PASSWORD`, `SPRING_PROFILES_ACTIVE`

## Health & Monitoring

- Actuator base: `/actuator`
  - Health: `/actuator/health`
  - Info: `/actuator/info`
  - Metrics: `/actuator/metrics`
  - Prometheus: `/actuator/prometheus`
- Default exposure: `health,info,metrics,prometheus`
- Test profile exposes: `*` (all endpoints)

## Metrics

- Micrometer + Prometheus registry
- Default tags: `application=${spring.application.name}`
- Scrape target: `/actuator/prometheus`

## Caching

- Spring Cache (ConcurrentMap)
- Cache name: `products`
- Annotations:
  - `ProductService.findById` → `@Cacheable("products")`
  - `ProductService.save/deleteById` → `@CacheEvict`

## Logging

- Config: `application.yml` (`logging.level.*`)
- Defaults: `com.skishop=DEBUG`, `org.springframework=INFO`, `org.hibernate.SQL=DEBUG`
- Override via `LOGGING_LEVEL_COM_SKISHOP`, etc.

## Database

- Default: PostgreSQL (localhost:5432, `skishop`)
- Test: H2 (in-memory, PostgreSQL mode)

## Endpoints (REST)

- Products API: `/products` (GET/POST/PUT/DELETE)
- See OpenAPI: `/v3/api-docs`, UI: `/swagger-ui.html`

## Startup Checks

1. `./mvnw -f spring-boot-app/pom.xml test`
2. `curl -f http://localhost:8080/actuator/health`
3. `curl -f http://localhost:8080/actuator/prometheus | head -n 5`

## Troubleshooting

- **Prometheus 404**: ensure `management.endpoint.prometheus.enabled=true` and registry on classpath
- **DB errors**: check env vars and connectivity to PostgreSQL
- **Caching not working**: verify `@EnableCaching` and cache name `products`

## Deployment (Docker)

```bash
# Compose
docker compose up -d --build

# Manual run (example)
docker build -t skishop-app:local .
docker run --rm -p 8080:8080 \
  -e DB_HOST=host.docker.internal -e DB_PORT=5432 -e DB_NAME=skishop \
  -e DB_USERNAME=postgres -e DB_PASSWORD=password \
  skishop-app:local
```
