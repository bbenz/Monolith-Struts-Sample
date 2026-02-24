# Spring Boot Migration Plan

## Migration Plan from Apache Struts 1.x to Spring Boot 3.x + Thymeleaf + Spring Data JPA

## Overview

### Progress (2026-01-22)

- [x] Phase0: Current Status Analysis (Action/JSP/DB Pattern Inventory)
- [x] Maven Wrapper Introduction (3.9.6) / Java 25 Support (`pom.xml` update)
- [x] Dockerfile: Temurin 25 + Tomcat 9.0 / Postgres 15
- [x] CI: GitHub Actions (Java 25 + ./mvnw)
- [x] Phase1: Add Spring Boot Prototype (`spring-boot-app/`)
    - Spring Boot 3.5.0 / Java 25 Prototype Creation
- [x] Phase2: JPA Implementation (All table entities/repositories/services, H2 tests green)
- [x] Phase3: REST Controller/DTO Complete, WebMvcTest Setup, Thymeleaf Template Skeleton Added
- [x] Phase4: Thymeleaf Full Implementation (JSP→Thymeleaf Full Screen Migration)
- [x] Phase5: Modern Java Refactoring (`.toList()`/`Map.of`/`var` etc.)
- [x] Phase6: Configuration & Exception Handling (REST/UI Exception Handler Separation, Error Pages Added)
- [x] Phase7: Test Expansion (UI/REST Exception Tests & Integration Tests Added, All Tests Green)
- [x] Phase8: Monitoring/Cache (Actuator+Prometheus, Spring Cache, HTTP Cache Performance Verification)
- [x] Phase9: Documentation & Operations Preparation (Operations/Architecture Docs, OpenAPI, Monitoring Procedures)
- [x] Phase10: Legacy Cleanup (Zero Struts References in Boot Confirmed, docs/legacy.md, Verification Scripts, README/Docs Updated)

## Java 25 LTS Runtime Upgrade Plan (#generate_upgrade_plan)

### Progress Summary (Phase 0 / Phase 1 / Phase 2 / Phase 3 / Phase 4 / Phase 5 / Phase 6 / Phase 7 / Phase 8 / Phase 9 / Phase 10)

- Phase 0: ✅ Completed (Action/JSP/DAO Inventory, JDK25/Maven/CI Policy Planning, Docker/Tomcat9 Planning)
- Phase 1: ✅ Completed (Spring Boot 3.5.0 Java25 Prototype `spring-boot-app/` Created, Package Skeleton Prepared)
- Phase 2: ✅ Completed (All Table JPA Entities/Repositories/Services Implemented, H2(PostgreSQL Mode) Tests Green)
    - Note: JPA entities do not use Records (due to lifecycle callbacks and mutable fields)
- Phase 3: ✅ Completed (REST Controllers/DTOs/Exception Handlers, WebMvcTest Setup, Thymeleaf Layout/Page Skeleton Created)
- Phase 4: ✅ Completed (Thymeleaf Full Implementation: UI Controllers/Templates/Header/Footer/Styles/Message Resources, View Tests Added)
    - Deliverables: `ViewController`, `AdminViewController`, `templates/**/*.html` Full Implementation, `messages.properties`, `static/css/main.css`
    - Tests: `ViewControllerTest`, `AdminViewControllerTest` Added, `CartControllerTest` Updated
- Phase 5: ✅ Completed (Modern Java Refactoring: `.toList()` Applied, `Map.of`/`var` Introduced, Pattern Checked)
- Phase 6: ✅ Completed (REST/UI Exception Handler Separation, `error/404.html`/`error/500.html` Added, Error View/JSON Response)
- Phase 7: ✅ Completed (UI/REST Exception Tests Added, Integration Test `ProductIntegrationTest` Added, All Tests Green)
- Phase 8: ✅ Completed (Actuator/Prometheus Exposed, Spring Cache Introduced, HTTP Cache Integration Test & Simple Performance Test Added)
- Phase 9: ✅ Completed (Operations/Architecture Documentation Added, OpenAPI Introduced, Monitoring Procedures/Endpoints Organized)
- Phase 10: ✅ Completed (Zero Struts References in Boot Confirmed, Archive Guidelines in docs/legacy.md, README/Docs Updated)

### Deliverables

- `docs/inventory/actions.txt` — Struts Action List
- `docs/inventory/jsp.txt` — JSP List
- `docs/inventory/dao.txt` — JDBC PreparedStatement Usage Locations
- `spring-boot-app/` — Spring Boot Project (Java 25, Boot 3.5.0)
    - `model/entity/*` — **All Tables** (roles, users, security_logs, categories, products, prices, inventory, carts, cart_items, payments, orders, order_items, shipments, returns, order_shipping, point_accounts, point_transactions, campaigns, coupons, coupon_usage, user_addresses, password_reset_tokens, shipping_methods, email_queue)
    - `repository/*Repository.java` — Spring Data JPA Repository for Each Entity
    - `service/*Service.java` — Service Implementation for Each Entity
    - `controller/*Controller.java` — REST Controllers (Products/Cart/Orders/Points/User/Address/Coupons/Returns/Admin)
    - `dto/*Request|*Response.java` — API DTOs
    - `exception/GlobalExceptionHandler.java` — Exception Handler
    - `src/test/java/com/skishop/repository/ProductRepositoryTest.java`
    - `src/test/java/com/skishop/service/ProductServiceTest.java`
    - `src/test/java/com/skishop/integration/ActuatorIntegrationTest.java`
    - `src/test/java/com/skishop/integration/ProductCachingHttpTest.java`
    - `src/test/java/com/skishop/integration/OpenApiIntegrationTest.java`
    - `src/test/java/com/skishop/SpringContextLoadsTest.java`
    - `src/test/java/com/skishop/controller/ProductControllerTest.java`
    - `src/test/java/com/skishop/controller/CartControllerTest.java`
    - `application-test.yml` — H2 (PostgreSQL mode) configuration
    - `src/main/java/com/skishop/config/OpenApiConfig.java`
    - `docs/operations.md`
    - `docs/architecture.md`
    - `docs/legacy.md`
    - `scripts/verify-no-struts.sh`
    - No Lombok Dependency (All entities have explicit getter/setter, public no-arg ctor)
    - `Product` `@PrePersist` sets `createdAt`/`updatedAt` to support NOT NULL constraints

### ✅ Tests

```bash
./mvnw -f spring-boot-app/pom.xml -B test
```

- Success: 2026-01-22 17:06 JST (`TESTS_OK`)

### Background

- Current: **JDK 5 + Maven 2.2.1 + Tomcat 6** (Dockerfile uses JDK1.5/Tomcat6)
- Target: **JDK 25 + Maven 3.9.x + Tomcat 9(javax)** to run and facilitate subsequent migration to Spring Boot 3

### Policy

1. **Build Tool Modernization**: Introduce Maven Wrapper, Maven 3.9.x+, JDK 25 standardization
2. **Compile Compatibility**: Update `maven-compiler-plugin` to 3.11+ and raise `source/target` to **8**+ (recommended 17/21)
3. **Application Server**: Adopt **Tomcat 9.0.x (javax version)** (avoid Tomcat 10+ due to jakarta.* incompatibility)
4. **Container**: Replace with multi-stage build based on `eclipse-temurin:25`
5. **CI**: Setup JDK 25 with GitHub Actions etc. and run tests

### Procedures

1. **Introduce JDK 25 / Maven 3.9.x**
     - Set `JAVA_HOME` to JDK 25 in local / CI
     - Add Maven Wrapper: `mvn -N io.takari:maven:0.7.7:wrapper -Dmaven=3.9.6`
2. **Update pom.xml**
     - Change `maven.compiler.source` / `target` to **8** (or **21**)
     - Plugin Update Example:

         ```xml
         <plugin>
             <groupId>org.apache.maven.plugins</groupId>
             <artifactId>maven-compiler-plugin</artifactId>
             <version>3.11.0</version>
             <configuration>
                 <source>21</source>
                 <target>21</target>
                 <encoding>UTF-8</encoding>
             </configuration>
         </plugin>
         <plugin>
             <groupId>org.apache.maven.plugins</groupId>
             <artifactId>maven-enforcer-plugin</artifactId>
             <version>3.4.1</version>
             <executions>
                 <execution>
                     <id>enforce-java</id>
                     <goals><goal>enforce</goal></goals>
                     <configuration>
                         <rules>
                             <requireJavaVersion><version>[21,)</version></requireJavaVersion>
                             <requireMavenVersion><version>[3.9,)</version></requireMavenVersion>
                         </rules>
                     </configuration>
                 </execution>
             </executions>
         </plugin>
         ```

     - Test: Run `mvn -DskipTests=false test` with JDK 25
3. **Minimum Dependency Updates** (for compatibility)
     - PostgreSQL JDBC: `42.7.4`
     - commons-fileupload: `1.5`
     - junit: `4.13.2` (JUnit5 migration comes later)
     - log4j: 1.x is EOL. Consider migration to SLF4J + Logback in the short term
4. **Dockerfile Update**
     - Base: `eclipse-temurin:25-jdk` (build) / `eclipse-temurin:25-jre` (runtime)
     - Tomcat: Use javax-compatible tags like `tomcat:9.0-jre17-temurin` (adopt JRE25 version if available)
     - `COPY target/*.war /usr/local/tomcat/webapps/ROOT.war`
5. **Local Operation Verification**
     - `mvn clean package`
     - `docker build -t skishop:java25 .`
     - `docker run -p 8080:8080 skishop:java25`
     - Manual verification of main screens/features
6. **CI/CD Setup** (GitHub Actions example)

     ```yaml
     jobs:
         build:
             runs-on: ubuntu-latest
             steps:
                 - uses: actions/checkout@v4
                 - uses: actions/setup-java@v4
                     with:
                         distribution: temurin
                         java-version: '21'
                 - run: ./mvnw -B clean verify
     ```

### Verification Checklist

- [ ] `mvn clean verify` succeeds with JDK 25
- [ ] Tomcat 9 + WAR starts with `docker run`
- [ ] Main screens, forms, DB access, email sending work
- [ ] No dependency library warnings (illegal access etc.)
- [ ] No reflection access warnings in logs, or suppressed with `--add-opens`

### Risks and Mitigation Strategies

| Risk | Description | Mitigation |
| --- | --- | --- |
| jakarta migration issues | Tomcat 10+ uses jakarta.* incompatible with Struts1 | Use **Tomcat 9** |
| Old JDBC driver | Untested with Java 25 | Update PostgreSQL JDBC to 42.7.4 |
| log4j 1.x vulnerabilities | EOL, numerous CVEs | Gradual migration to SLF4J + Logback |
| Build compatibility | `--release` 5 not possible | Raise `source/target` to 8+ |

### Reference Commands

```bash
# Add Maven Wrapper
mvn -N io.takari:maven:0.7.7:wrapper -Dmaven=3.9.6

# Build
./mvnw -B clean package

# Docker build/start
docker build -t skishop:java25 .
docker run --rm -p 8080:8080 skishop:java25
```

This project is an old Java application using Apache Struts 1.3.10, targeting Java 1.5. This document outlines the complete migration plan to **Java 25 + Spring Boot 3.5.x + Thymeleaf + Spring Data JPA**.

## Current State

### Java Version

- **Current**: Java 1.5 (released 2004, end of support)
- **Migration Target**: Java 25 LTS (released September 2023, LTS support until September 2031)

### Framework

- **Current**: Apache Struts 1.3.10 (released 2008, EOL, numerous known vulnerabilities)
- **Migration Target**: Spring Boot 3.5.x (latest stable, long-term support)

### Current Dependency Versions

| Category | Library | Current Version | Latest LTS Version | Notes |
| --- | --- | --- | --- | --- |
| **Framework** | | | | |
| | Apache Struts Core | 1.3.10 | N/A | EOL, migration recommended |
| | Apache Struts Taglib | 1.3.10 | N/A | EOL, migration recommended |
| | Apache Struts Tiles | 1.3.10 | N/A | EOL, migration recommended |
| | Apache Struts Extras | 1.3.10 | N/A | EOL, migration recommended |
| **Database Connection** | | | | |
| | commons-dbcp | 1.2.2 | 2.12.0 | Migration to DBCP2 recommended |
| | commons-pool | 1.2 | 2.12.0 | Migration to Pool2 recommended |
| | commons-dbutils | 1.1 | 1.8.1 | Upgradeable |
| | PostgreSQL JDBC | 9.2-1004-jdbc3 | 42.7.4 | Major upgrade |
| **File Upload** | | | | |
| | commons-fileupload | 1.3.3 | 1.5 | Security fixes available |
| **Logging** | | | | |
| | log4j | 1.2.17 | N/A | Migration to Log4j2 2.23.1 recommended |
| **Mail** | | | | |
| | javax.mail | 1.4.7 | Jakarta Mail 2.1.3 | Migration to Jakarta EE |
| **View Template/Web** | | | | |
| | jsp-api | 2.1 | Thymeleaf 3.1.x | Migration from JSP to Thymeleaf |
| | servlet-api | 2.5 | Spring Boot Embedded (Tomcat 10.1.x) | Included in Spring Boot Starter |
| | - | - | Spring Web MVC 6.1.x | RESTful Web Service support |
| **Testing** | | | | |
| | JUnit | 4.12 | JUnit 5.10.2 | Migration to JUnit Jupiter |
| | H2 Database | 1.3.176 | 2.2.224 | For testing |
| | StrutsTestCase | 2.1.4-1.2-2.4 | N/A | Struts-dependent, consider removal |

### Current State Assessment (Issue Summary)

| Item | Current State | Issues |
| --- | --- | --- |
| Java | 1.5 | EOL, latest libraries incompatible |
| Framework | Struts 1.3.10 | EOL, numerous vulnerabilities |
| Template | JSP + Struts Taglib | Obsolete, low maintainability |
| Data Access | JDBC + Commons DBUtils | Lots of boilerplate, manual Tx management |
| Connection Pool | Commons DBCP 1.x | DBCP2/HikariCP incompatible, performance/stability issues |
| Logging | Log4j 1.2.17 | EOL, need migration to Logback/SLF4J |
| Testing | JUnit 4 + StrutsTestCase | Need migration to JUnit 5/Spring Test |

## Migration Strategy

### Why Choose Spring Boot

**Apache Struts 1.x reached EOL in 2013 and has numerous known vulnerabilities.** Partial dependency upgrades won't solve fundamental problems.

#### Reasons to Recommend Complete Migration to Spring Boot 3.5.x

1. **Security**: Continuous security updates and support
2. **Community**: Largest Java community with abundant documentation
3. **Modern Technology**: Can leverage all Java 25 features
4. **Productivity**: Fast development with auto-configuration, embedded server, development tools
5. **Future-proof**: Clear migration path to microservices, cloud-native
6. **Ecosystem**: Rich Spring Boot starters, integration support

### Target Technology Stack

| Component | Struts 1.x | Spring Boot 3.5.x |
| --- | --- | --- |
| **Framework** | Apache Struts 1.3.10 | Spring Boot 3.5.x + Spring MVC 6.1.x |
| **Java Version** | Java 1.5 | Java 25 LTS |
| **View Template** | JSP + Struts Taglib | Thymeleaf 3.1.x |
| **Data Access** | JDBC + Commons DBUtils | Spring Data JPA 3.2.x + Hibernate 6.4.x |
| **Connection Pool** | Commons DBCP 1.x | HikariCP (Spring Boot default) |
| **Validation** | Commons Validator | Bean Validation 3.0 (Hibernate Validator) |
| **Logging** | Log4j 1.2.17 | Logback (Spring Boot default) + SLF4J |
| **Dependency Injection** | None | Spring IoC Container |
| **Testing** | JUnit 4 + StrutsTestCase | JUnit 5 + Spring Boot Test |
| **Build Tool** | Maven 2.x series | Maven 3.9.x |
| **Application Server** | External Tomcat 6/7 | Embedded Tomcat 10.1.x |

## Struts 1.x and Spring Boot Component Mapping

### Architecture Mapping

| Struts 1.x Component | Spring Boot Equivalent | Description |
| --- | --- | --- |
| **Action** | `@Controller` + `@RequestMapping` | Request processing |
| **ActionForm** | `@ModelAttribute` + Bean Validation | Form data binding |
| **struts-config.xml** | Java Config (`@Configuration`) | Application configuration |
| **ActionForward** | `ModelAndView` / `return "viewName"` | View navigation |
| **ActionMapping** | `@RequestMapping` / `@GetMapping` / `@PostMapping` | URL mapping |
| **ActionServlet** | `DispatcherServlet` (auto-configured) | Front controller |
| **JSP + Struts Tags** | Thymeleaf templates | View rendering |
| **Validator Framework** | Bean Validation + `@Valid` | Input validation |
| **MessageResources** | `MessageSource` + `messages.properties` | Internationalization |
| **DAO (manual JDBC)** | Spring Data JPA Repository | Data access |
| **DataSource (DBCP)** | HikariCP (auto-configured) | Connection pool |

## Progressive Migration Plan

### Phase 0: Preparation Phase (1 week) - Currently in Progress

#### Task Details

1. **Current State Analysis**
   - Create list of all Action classes
   - Create list of all JSP pages
   - Investigate database access patterns
   - Confirm external library dependencies

2. **Environment Setup**
   - Install JDK 25
   - Prepare IDE (IntelliJ IDEA / Eclipse)
   - Decide Git branching strategy (e.g., `feature/spring-boot-migration`)

3. **Create Spring Boot Project**
   - Generate basic project with Spring Initializr
   - Add necessary dependencies

#### Spring Initializr Settings

```text
Project: Maven
Language: Java
Spring Boot: 3.2.x (latest stable)
Java: 21
Packaging: War (for compatibility with existing WAR deployment, can change to Jar later)

Dependencies:
- Spring Web
- Thymeleaf
- Spring Data JPA
- PostgreSQL Driver
- Validation
- Spring Boot DevTools
- Lombok (optional, reduces boilerplate code)
- Spring Boot Actuator (optional, for monitoring)
```

#### Example Generated pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.3</version>
        <relativePath/>
    </parent>
    
    <groupId>com.skishop</groupId>
    <artifactId>skishop-app</artifactId>
    <version>2.0.0</version>
    <packaging>war</packaging>
    <name>SkiShop Application</name>
    
    <properties>
        <java.version>21</java.version>
    </properties>
    
    <dependencies>
        <!-- Spring Boot Web (includes Spring MVC) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <!-- Thymeleaf template engine -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        
        <!-- Spring Data JPA -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        
        <!-- Bean Validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        
        <!-- PostgreSQL driver -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
        
        <!-- Development tools (hot reload, etc.) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        
        <!-- Lombok (optional) -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        
        <!-- Email sending -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>
        
        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        
        <!-- H2 Database (for testing) -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### Phase 1: Project Structure and Application Entry Point Creation (1 week)

#### Create Spring Boot Main Class

```java
package com.skishop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SkiShopApplication extends SpringBootServletInitializer {
    
    public static void main(String[] args) {
        SpringApplication.run(SkiShopApplication.class, args);
    }
}
```

#### application.yml Configuration

```yaml
spring:
  application:
    name: skishop-app
  
  # DataSource configuration
  datasource:
    url: jdbc:postgresql://localhost:5432/skishop
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:password}
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      connection-timeout: 30000
  
  # JPA configuration
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect
  
  # Thymeleaf configuration
  thymeleaf:
    cache: false  # Set to false during development
    prefix: classpath:/templates/
    suffix: .html
    mode: HTML
  
  # Mail configuration
  mail:
    host: ${MAIL_HOST:smtp.example.com}
    port: ${MAIL_PORT:587}
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true

# Logging configuration
logging:
  level:
    com.skishop: DEBUG
    org.springframework: INFO
    org.hibernate.SQL: DEBUG
```

#### Create Package Structure

```text
src/main/java/com/skishop/
├── SkiShopApplication.java
├── config/              # Configuration classes
│   ├── WebConfig.java
│   └── SecurityConfig.java (if needed)
├── controller/          # Struts Action → Controller
├── model/              # Entity classes
│   └── entity/
├── repository/         # Spring Data JPA repositories
├── service/            # Business logic
│   └── impl/
├── dto/                # Data Transfer Objects
└── exception/          # Exception handling

src/main/resources/
├── application.yml
├── messages.properties
├── templates/          # Thymeleaf templates
│   ├── fragments/      # Common components
│   ├── layout/         # Layouts
│   └── pages/          # Pages
└── static/
    ├── css/
    ├── js/
    └── images/
```

### Phase 2: Data Access Layer Migration (2 weeks)

#### JPA Entity Class Creation Example

```java
package com.skishop.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Product name is required")
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(length = 500)
    private String description;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Min(value = 0, message = "Stock quantity must be 0 or greater")
    @Column(nullable = false)
    private Integer stockQuantity;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
```

#### Create Spring Data JPA Repository

```java
package com.skishop.repository;

import com.skishop.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Query auto-generated from method name
    List<Product> findByNameContaining(String keyword);
    
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    Optional<Product> findByName(String name);
    
    // Custom query
    @Query("SELECT p FROM Product p WHERE p.stockQuantity > 0 ORDER BY p.createdAt DESC")
    List<Product> findAvailableProducts();
    
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% OR p.description LIKE %:keyword%")
    List<Product> searchProducts(@Param("keyword") String keyword);
}
```

#### Create Service Layer

```java
package com.skishop.service.impl;

import com.skishop.model.entity.Product;
import com.skishop.repository.ProductRepository;
import com.skishop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    
    @Override
    public List<Product> getAllProducts() {
        log.debug("Fetching all products");
        return productRepository.findAll();
    }
    
    @Override
    public Optional<Product> getProductById(Long id) {
        log.debug("Fetching product by id: {}", id);
        return productRepository.findById(id);
    }
    
    @Override
    @Transactional
    public Product saveProduct(Product product) {
        log.info("Saving product: {}", product.getName());
        return productRepository.save(product);
    }
    
    @Override
    @Transactional
    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);
        productRepository.deleteById(id);
    }
}
```

### Phase 3: Controller Layer Migration (3 weeks)

#### Migrate from Struts Action to Spring MVC Controller

**Spring Boot Controller example:**

```java
package com.skishop.controller;

import com.skishop.dto.ProductFormDTO;
import com.skishop.model.entity.Product;
import com.skishop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    
    private final ProductService productService;
    
    @GetMapping
    public String listProducts(Model model) {
        log.debug("Displaying product list");
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("productForm", new ProductFormDTO());
        return "products/form";
    }
    
    @PostMapping
    public String createProduct(@Valid @ModelAttribute("productForm") ProductFormDTO form,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "products/form";
        }
        
        Product product = new Product();
        product.setName(form.getName());
        product.setDescription(form.getDescription());
        product.setPrice(form.getPrice());
        product.setStockQuantity(form.getStockQuantity());
        
        productService.saveProduct(product);
        
        redirectAttributes.addFlashAttribute("message", "Product registered successfully");
        return "redirect:/products";
    }
}
```

#### Create DTOs

```java
package com.skishop.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductFormDTO {
    
    private Long id;
    
    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name must be 100 characters or less")
    private String name;
    
    @Size(max = 500, message = "Description must be 500 characters or less")
    private String description;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;
    
    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity must be 0 or greater")
    private Integer stockQuantity;
}
```

### Phase 4: View Layer Migration (JSP → Thymeleaf) (3 weeks)

#### Thymeleaf Template Example (Product List)

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" th:replace="~{layout/main :: layout(~{::title}, ~{::content})}">
<head>
    <title th:text="#{products.list.title}">Product List</title>
</head>
<body>
    <div th:fragment="content">
        <h1 th:text="#{products.list.header}">Product List</h1>
        
        <div class="alert alert-success" th:if="${message}" th:text="${message}"></div>
        
        <table class="table">
            <thead>
                <tr>
                    <th th:text="#{product.name}">Product Name</th>
                    <th th:text="#{product.price}">Price</th>
                    <th th:text="#{product.stock}">Stock</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <tr th:each="product : ${products}">
                    <td th:text="${product.name}">Product Name</td>
                    <td th:text="${#numbers.formatCurrency(product.price)}">¥1,000</td>
                    <td th:text="${product.stockQuantity}">10</td>
                    <td>
                        <a th:href="@{/products/{id}/edit(id=${product.id})}" class="btn btn-sm btn-primary">Edit</a>
                    </td>
                </tr>
            </tbody>
        </table>
        
        <a th:href="@{/products/new}" class="btn btn-primary">New Registration</a>
    </div>
</body>
</html>
```

#### Struts Taglib and Thymeleaf Correspondence Table

| Struts 1.x Tag | Thymeleaf Equivalent | Description |
| --- | --- | --- |
| `<bean:write name="var"/>` | `th:text="${var}"` | Variable output |
| `<bean:message key="key"/>` | `th:text="#{key}"` | Message resource |
| `<html:link action="/path">` | `th:href="@{/path}"` | Link |
| `<html:form action="/submit">` | `th:action="@{/submit}" method="post"` | Form |
| `<html:text property="name"/>` | `th:field="*{name}"` | Text input |
| `<html:errors property="name"/>` | `th:errors="*{name}"` | Validation errors |
| `<logic:iterate id="item" name="list">` | `th:each="item : ${list}"` | Loop |
| `<logic:present name="var">` | `th:if="${var != null}"` | Presence check |
| `<logic:notPresent name="var">` | `th:if="${var == null}"` | Absence check |
| `<logic:equal name="var" value="val">` | `th:if="${var == 'val'}"` | Value comparison |

### Phase 5: Modern Java Refactoring (1 week)

#### Purpose

- Update Java 5-era code style and APIs to modern Java 25 practices to improve readability, safety, and performance.

#### Checkpoint

| Item | Before example | After example | Notes |
| --- | --- | --- | --- |
| Resource cleanup | `try { ... } finally { close(); }` | `try (var in = ...) { ... }` | try-with-resources |
| Type inference | `Map<String, List<String>> map = new HashMap<String, List<String>>();` | `var map = new HashMap<String, List<String>>();` | diamond + var |
| instanceof | `if (obj instanceof Foo) { Foo f = (Foo) obj; }` | `if (obj instanceof Foo f) { ... }` | pattern matching |
| Strings | `"line1\nline2"` | `"""line1\nline2"""` | text blocks |
| Date/Time | `Date/Calendar` | `LocalDateTime/Instant` | java.time |
| Collections | `new ArrayList<>()` then add | `List.of(...)` | Immutable list |
| Collection factory | `new HashSet<>(); add...` | `Set.of(...) / Map.of(...)` | Immutable Set/Map |
| Map initialization | `if (!map.containsKey(k)) map.put(k, ...)` | `map.computeIfAbsent(k, ...)` | Java 8 |
| Loops | `for (String s : list) { ... }` | `list.stream().map(...).toList()` | Streams (use appropriately) |
| DTO | `class Foo { ... }` | `record Foo(...) {}` | DTO/Value Object only |
| HTTP | `HttpURLConnection` | `HttpClient` | Java 11 |
| Concurrency | `ExecutorService` | `virtual threads (Threads.ofVirtual().factory())` | To be considered |
| Lambda/Method reference | `new Runnable(){ public void run(){...}}` | `Runnable r = () -> {...}` / `System.out::println` | Java 8 |
| Functional IF | Multiple custom IFs | `java.util.function.*` | Reuse and unification |
| switch syntax | `switch(x){case A: ... break;}` | `switch (x) { case A -> ...; default -> ...; }` | switch expressions |
| switch pattern | Type branching with `if/else` | `switch (obj) { case String s -> ... }` | Java 25 |
| Multi-catch | Duplicate catch blocks | `catch (IOException\|SQLException e)` | Java 7 |
| Numeric literals | `1000000` | `1_000_000` | Readability |
| Optional | `if (obj == null) ...` | `Optional.ofNullable(obj).ifPresent(...)` | null safety |
| Stream extension | `collect(Collectors.toList())` | `.toList()` | Java 16 |
| Stream extension 2 | Procedural for loop | `stream().takeWhile(...).dropWhile(...)` | Java 9 |
| Optional extension | `if (obj == null) ...` | `opt.ifPresentOrElse(...); opt.orElseThrow(); opt.stream()` | Java 9/10 |
| NIO.2 | `new File(...)` | `Path/Files.walk(...)` | Java 7 |
| CompletableFuture | `Future` + `get()` | `CompletableFuture.supplyAsync(...)` | Asynchronous processing |
| Sealed | Control with `abstract class` | `sealed interface Shape permits Circle, Square {}` | Java 17 |
| String utilities | `trim().isEmpty()` | `isBlank()/strip()/lines()/repeat()` | Java 11 |
| Random | `new Random()` | `ThreadLocalRandom.current()` / `RandomGenerator` | Thread-safe/reproducibility |
| finalize | `protected void finalize()` | `Cleaner` / try-with-resources | Java 9+ deprecated |
| Record patterns | `if (obj instanceof Point) { ... }` | `if (obj instanceof Point(int x, int y)) { ... }` | Java 25 |
| String templates | `"Hello " + name` | `STR."Hello ${name}"` | Java 25 (Preview) |

#### Implementation Steps

1. Detect candidates with static analysis (IDE inspections, SpotBugs, Checkstyle, SonarLint)
2. Apply IDE refactorings + auto-fix (try-with-resources, diamond, var, pattern matching, text blocks)
3. Manual review (design decisions for java.time/Streams/Optional/records application)
4. Run all tests and verify performance

### Phase 6: Configuration and Other Migrations (1 week)

#### Exception Handling

```java
package com.skishop.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        log.error("Resource not found: {}", ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "error/404";
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralError(Exception ex, Model model) {
        log.error("Unexpected error occurred", ex);
        model.addAttribute("error", "An unexpected error occurred");
        return "error/500";
    }
}
```

### Phase 7: Test Expansion (2 weeks)

#### Repository Tests

```java
package com.skishop.repository;

import com.skishop.model.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Test
    void testFindByNameContaining() {
        // Given
        Product product = new Product();
        product.setName("Ski Boots");
        product.setPrice(BigDecimal.valueOf(200.00));
        product.setStockQuantity(10);
        productRepository.save(product);
        
        // When
        List<Product> found = productRepository.findByNameContaining("Ski");
        
        // Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getName()).isEqualTo("Ski Boots");
    }
}
```

#### Service Tests

```java
package com.skishop.service;

import com.skishop.model.entity.Product;
import com.skishop.repository.ProductRepository;
import com.skishop.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    
    @Mock
    private ProductRepository productRepository;
    
    @InjectMocks
    private ProductServiceImpl productService;
    
    private Product testProduct;
    
    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(BigDecimal.valueOf(100.00));
        testProduct.setStockQuantity(5);
    }
    
    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        // Given
        when(productRepository.findAll()).thenReturn(Arrays.asList(testProduct));
        
        // When
        List<Product> products = productService.getAllProducts();
        
        // Then
        assertThat(products).hasSize(1);
        verify(productRepository, times(1)).findAll();
    }
    
    @Test
    void saveProduct_ShouldSaveAndReturnProduct() {
        // Given
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        
        // When
        Product saved = productService.saveProduct(testProduct);
        
        // Then
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Product");
        verify(productRepository, times(1)).save(testProduct);
    }
}
```

#### Controller Tests

```java
package com.skishop.controller;

import com.skishop.model.entity.Product;
import com.skishop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ProductService productService;
    
    @Test
    void listProducts_ShouldReturnProductListView() throws Exception {
        // Given
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100.00));
        product.setStockQuantity(10);
        
        when(productService.getAllProducts()).thenReturn(Arrays.asList(product));
        
        // When & Then
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/list"))
                .andExpect(model().attributeExists("products"));
    }
}
```

#### Integration Tests

```java
package com.skishop;

import com.skishop.model.entity.Product;
import com.skishop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProductIntegrationTest {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Test
    void testProductCreationAndRetrieval() {
        // Given
        Product product = new Product();
        product.setName("Integration Test Product");
        product.setPrice(BigDecimal.valueOf(150.00));
        product.setStockQuantity(20);
        productRepository.save(product);
        
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(
            "http://localhost:" + port + "/products",
            String.class
        );
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Integration Test Product");
    }
}
```

### Phase 8: Performance Testing and Tuning (1 week)

#### Perform Performance Testing

##### Load Testing with JMeter

```xml
<!-- Add to pom.xml -->
<dependency>
    <groupId>org.apache.jmeter</groupId>
    <artifactId>ApacheJMeter_core</artifactId>
    <version>5.6.3</version>
    <scope>test</scope>
</dependency>
```

##### Monitor Application Metrics

```yaml
# Add to application.yml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

##### Performance Test Scenarios

1. **Concurrent Connection Test**
   - 100 simultaneous user access
   - Response time < 500ms
   - Error rate < 1%

2. **Database Query Optimization**
   - Detect and fix N+1 problems
   - Index optimization
   - Query plan analysis

3. **Cache Strategy**

```java
@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
            new ConcurrentMapCache("products"),
            new ConcurrentMapCache("categories")
        ));
        return cacheManager;
    }
}
```

```java
@Service
public class ProductServiceImpl implements ProductService {
    
    @Cacheable(value = "products", key = "#id")
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    @CacheEvict(value = "products", key = "#product.id")
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}
```

#### Performance Tuning Checklist

- [ ] Optimize database connection pool settings (HikariCP)
- [ ] Optimize JPA/Hibernate queries (Lazy Loading, Eager Loading)
- [ ] Create appropriate indexes
- [ ] Implement cache strategy
- [ ] Reduce unnecessary log output
- [ ] Compress and cache static resources
- [ ] Adjust JVM heap size
- [ ] Optimize garbage collection

### Phase 9: Documentation and Operations Preparation (1 week)

#### Documents to Create

##### 1. Architecture Documentation

**Contents:**

- Overall system architecture diagram
- Dependencies between components
- Data flow diagram
- Deployment architecture

##### 2. API Specification

```java
// Automatic API documentation using SpringDoc
@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("SkiShop API")
                .version("2.0.0")
                .description("SkiShop application API after Spring Boot migration"));
    }
}
```

```xml
<!-- Add to pom.xml -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

##### 3. Operations Manual

**Contents:**

- Application start/stop procedures
- How to check logs
- Troubleshooting guide
- Backup/restore procedures
- Deploy procedures

**Startup command examples:**

```bash
# Development environment
mvn spring-boot:run

# Production environment (JAR file)
java -jar -Xmx2g -Xms1g \
  -Dspring.profiles.active=production \
  skishop-app-2.0.0.jar

# Production environment (Docker container)
docker run -d \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=production \
  -e DB_USERNAME=prod_user \
  -e DB_PASSWORD=prod_pass \
  skishop-app:2.0.0
```

##### 4. Developer Guide

**Content:**

- Project structure explanation
- Coding standards
- How to write tests
- Local development environment setup
- Common problems and solutions

##### 5. Migration Report

**Content:**

- Pre and post-migration comparison
- Problems encountered and solutions
- Remaining technical debt
- Future improvement proposals

#### Operations Monitoring Configuration

```yaml
# application-production.yml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus,loggers
  endpoint:
    health:
      show-details: always
  health:
    db:
      enabled: true
    diskspace:
      enabled: true

logging:
  level:
    root: WARN
    com.skishop: INFO
  file:
    name: /var/log/skishop/application.log
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
```

#### Deployment Dockerfile

```dockerfile
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/skishop-app-2.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### CI/CD Pipeline (GitHub Actions example)

```yaml
name: Build and Deploy

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 25
      uses: actions/setup-java@v4
      with:
        java-version: '25'
        distribution: 'temurin'
    
    - name: Build with Maven
      run: mvn clean package -DskipTests
    
    - name: Run tests
      run: mvn test
    
    - name: Build Docker image
      run: docker build -t skishop-app:${{ github.sha }} .
    
    - name: Push to registry
      run: |
        echo ${{ secrets.DOCKER_PASSWORD }} | docker login -u ${{ secrets.DOCKER_USERNAME }} --password-stdin
        docker push skishop-app:${{ github.sha }}
```

### Phase 10: Legacy Code Cleanup and Final Verification (1 week)

#### Cleanup Purpose

After completing migration through Phase 8 and confirming that the Spring Boot application is working correctly, remove old Struts-related code, configuration files, and JSP-related code that are no longer in use. This organizes the codebase and improves maintainability.

#### Files and Code to Delete

##### 1. Struts-Related Configuration Files

```text
Delete targets:
- WEB-INF/struts-config.xml
- WEB-INF/validation.xml
- WEB-INF/validator-rules.xml
- WEB-INF/tiles-defs.xml
- src/main/resources/struts.properties
- src/main/resources/validation.properties
```

##### 2. Struts Action Classes

```text
Delete target directories:
- src/main/java/com/*/action/
- src/main/java/com/*/struts/

Verification items:
- All Action classes have been migrated to Spring MVC Controllers
- Business logic has been extracted to Service layer
```

##### 3. ActionForm Classes

```text
Delete targets:
- src/main/java/com/*/form/
- *ActionForm.java

Verification items:
- All form classes have been migrated to DTOs
- Bean Validation annotations are applied
```

##### 4. JSP Files and Struts Taglib

```text
Delete targets:
- src/main/webapp/**/*.jsp
- src/main/webapp/WEB-INF/tags/
- WEB-INF/tld/*.tld (Struts Tag Library definitions)

Verification items:
- All JSP files have been migrated to Thymeleaf templates
- Screen display functionality verification completed
```

##### 5. Struts-Related Dependencies (pom.xml)

```xml
Dependencies to delete:
<dependencies>
    <!-- Delete: Apache Struts -->
    <dependency>
        <groupId>struts</groupId>
        <artifactId>struts</artifactId>
        <version>1.3.10</version>
    </dependency>
    
    <!-- Delete: Commons Validator -->
    <dependency>
        <groupId>commons-validator</groupId>
        <artifactId>commons-validator</artifactId>
        <version>1.3.1</version>
    </dependency>
    
    <!-- Delete: Commons Digester -->
    <dependency>
        <groupId>commons-digester</groupId>
        <artifactId>commons-digester</artifactId>
        <version>1.8</version>
    </dependency>
    
    <!-- Delete: Commons BeanUtils -->
    <dependency>
        <groupId>commons-beanutils</groupId>
        <artifactId>commons-beanutils</artifactId>
        <version>1.8.0</version>
    </dependency>
    
    <!-- Delete: Commons Chain -->
    <dependency>
        <groupId>commons-chain</groupId>
        <artifactId>commons-chain</artifactId>
        <version>1.2</version>
    </dependency>
    
    <!-- Delete: Servlet API (included in Spring Boot) -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>servlet-api</artifactId>
        <version>2.5</version>
    </dependency>
    
    <!-- Delete: JSP API (migrated to Thymeleaf) -->
    <dependency>
        <groupId>javax.servlet.jsp</groupId>
        <artifactId>jsp-api</artifactId>
        <version>2.1</version>
    </dependency>
    
    <!-- Delete: JSTL (migrated to Thymeleaf) -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>jstl</artifactId>
        <version>1.2</version>
    </dependency>
    
    <!-- Delete: StrutsTestCase -->
    <dependency>
        <groupId>strutstestcase</groupId>
        <artifactId>strutstestcase</artifactId>
        <version>2.1.4-1.2-2.4</version>
    </dependency>
</dependencies>
```

##### 6. Update web.xml

```xml
web.xml content to delete:
<!-- Delete: Struts ActionServlet configuration -->
<servlet>
    <servlet-name>action</servlet-name>
    <servlet-class>org.apache.struts.action.ActionServlet</servlet-class>
    <init-param>
        <param-name>config</param-name>
        <param-value>/WEB-INF/struts-config.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
    <servlet-name>action</servlet-name>
    <url-pattern>*.do</url-pattern>
</servlet-mapping>

<!-- Delete: Struts TagLib configuration -->
<jsp-config>
    <taglib>
        <taglib-uri>/tags/struts-bean</taglib-uri>
        <taglib-location>/WEB-INF/struts-bean.tld</taglib-location>
    </taglib>
    <!-- Other Struts tag library definitions -->
</jsp-config>

Note: Spring Boot typically does not require web.xml,
      but if you need to keep existing configuration,
      delete only Struts-related settings.
```

##### 7. Other Configuration Files

```text
Files to delete:
- src/main/resources/ApplicationResources.properties (migrated to messages.properties)
- src/main/webapp/WEB-INF/classes/ (unnecessary class files)
```

#### Cleanup Procedures

##### Step 1: Preparation (1 day)

```bash
# 1. Create complete backup
git checkout -b backup/before-cleanup
git add .
git commit -m "Backup before legacy code cleanup"
git push origin backup/before-cleanup

# 2. Create cleanup branch
git checkout main
git checkout -b feature/cleanup-legacy-code

# 3. Verify current operation
mvn clean test
mvn spring-boot:run
# Verify all features
```

##### Step 2: Delete Struts-related Files (2 days)

```bash
# Delete Struts configuration files
rm -f src/main/webapp/WEB-INF/struts-config.xml
rm -f src/main/webapp/WEB-INF/validation.xml
rm -f src/main/webapp/WEB-INF/validator-rules.xml
rm -f src/main/webapp/WEB-INF/tiles-defs.xml
rm -rf src/main/webapp/WEB-INF/tld/

# Delete Struts Java code
find src/main/java -type d -name "action" -exec rm -rf {} +
find src/main/java -type d -name "form" -exec rm -rf {} +
find src/main/java -name "*Action.java" -delete
find src/main/java -name "*ActionForm.java" -delete

# Must run build and test after each deletion
mvn clean compile
mvn test
```

##### Step 3: Delete JSP-related Files (2 days)

```bash
# Verify existence of Thymeleaf templates before deleting all JSP files
find src/main/resources/templates -name "*.html" | wc -l

# Delete JSP files
rm -rf src/main/webapp/*.jsp
rm -rf src/main/webapp/WEB-INF/jsp/
rm -rf src/main/webapp/WEB-INF/pages/

# Delete tag files
rm -rf src/main/webapp/WEB-INF/tags/

# After each deletion, verify operation
mvn spring-boot:run
# Verify all screens in browser
```

##### Step 4: pom.xml Cleanup (1 day)

```bash
# Remove unnecessary dependencies from pom.xml
# Edit manually or check with the following commands

# Detect unused dependencies
mvn dependency:analyze

# Confirm no issues with build and test
mvn clean install
mvn test

# Check dependency tree
mvn dependency:tree
```

##### Step 5: Update or Delete web.xml (1 day)

```bash
# web.xml is basically unnecessary in Spring Boot
# Delete web.xml if it becomes empty after removing Struts configuration
rm -f src/main/webapp/WEB-INF/web.xml

# Or update to keep only necessary configuration
# (Filter settings, etc., if needed after Spring Boot migration)
```

#### Post-Cleanup Verification Checklist

##### 1. Build and Test Verification

- [ ] `mvn clean compile` succeeds
- [ ] All tests pass with `mvn test`
- [ ] WAR/JAR file is generated properly with `mvn package`
- [ ] No compilation errors
- [ ] Verify and address warning messages

##### 2. Application Startup Verification

- [ ] Application starts properly with `mvn spring-boot:run`
- [ ] No errors in startup log
- [ ] Spring Boot banner is displayed
- [ ] All Beans are loaded properly
- [ ] Database connection is established

##### 3. Functional Test Verification

- [ ] All screens display properly (Thymeleaf templates)
- [ ] All form submissions work properly
- [ ] Database insert/update/delete operations work properly
- [ ] File upload functionality works (if applicable)
- [ ] Email sending functionality works (if applicable)
- [ ] Session management functions properly
- [ ] Error handling works properly

##### 4. Performance Test Verification

- [ ] Response time has not degraded
- [ ] Memory usage is appropriate
- [ ] CPU utilization is within normal range
- [ ] Database connection pool operates properly

##### 5. Security Test Verification

- [ ] Authentication and authorization work properly
- [ ] XSS protection is functioning (Thymeleaf auto-escaping)
- [ ] CSRF protection is functioning (if needed)
- [ ] SQL injection protection is functioning (using JPA)

##### 6. Codebase Verification

- [ ] No Struts-related import statements remain
- [ ] No unused classes remain
- [ ] Verify and address TODO/FIXME comments
- [ ] Static code analysis (SonarQube etc.)

```bash
# Verify no references to Struts
grep -r "import org.apache.struts" src/
grep -r "struts" pom.xml

# Verify no JSP-related references
grep -r "import javax.servlet.jsp" src/
grep -r "jsp-api" pom.xml

# Verify search results are empty
```

#### Post-Cleanup Final Processing

##### 1. Update Documentation

```markdown
Documents to update:
- README.md (startup method, technology stack updates)
- CHANGELOG.md (migration history record)
- API specification (OpenAPI/Swagger)
- Operations manual (deployment procedure updates)
```

##### 2. Commit Changes and Create Pull Request

```bash
# Stage changes
git add .

# Commit
git commit -m "chore: Remove legacy Struts and JSP code after Spring Boot migration

- Remove all Struts Action classes and ActionForm classes
- Remove all JSP files and Struts TagLib configurations
- Remove Struts dependencies from pom.xml
- Clean up web.xml (remove Struts servlet configuration)
- Verify all functionality works with Spring Boot and Thymeleaf

Closes #XXX"

# Push to remote
git push origin feature/cleanup-legacy-code

# Create pull request and request review
```

##### 3. Final Verification Before Production Deployment

- [ ] Full test implementation in staging environment
- [ ] Load test implementation
- [ ] Security scan implementation
- [ ] Demo to stakeholders
- [ ] Final deployment plan verification
- [ ] Rollback procedure preparation

##### 4. Production Environment Deployment

```bash
# Create tag
git tag -a v2.0.0 -m "Spring Boot migration completed - Legacy code removed"
git push origin v2.0.0

# Production deployment (via CI/CD pipeline)
# Or manual deployment
```

#### Effects of Cleanup

##### Quantitative Effects

| Item | Reduction (Estimated) | Notes |
| --- | --- | --- |
| Code lines | 30-50% reduction | Action, ActionForm, JSP deletion |
| Dependency libraries | 10-15 libraries reduced | Struts-related library deletion |
| WAR file size | 20-30% reduction | Unnecessary libraries and JSP deletion |
| Build time | 10-20% shorter | Dependency reduction |
| Startup time | Improved | Spring Boot optimization |

##### Qualitative Effects

1. **Improved Maintainability**
   - Elimination of dual management
   - Codebase consistency
   - Easier understanding for new developers

2. **Improved Security**
   - Removal of vulnerable libraries
   - Reduced attack surface

3. **Improved Development Efficiency**
   - Clear architecture
   - Modern development environment
   - Improved testability

4. **Resolution of Technical Debt**
   - EOL framework removal
   - Legacy code removal
   - Investment in the future

#### Troubleshooting

##### Problem 1: Build errors occur after deletion

**Cause**: Some code depends on deleted classes

**Solution**:

```bash
# Identify dependent classes from error log
mvn clean compile 2>&1 | grep "cannot find symbol"

# Fix relevant locations
# - Replace with Spring Boot equivalent functionality
# - Delete if unnecessary code
```

##### Problem 2: Tests fail

**Cause**: Test code depends on StrutsTestCase

**Solution**:

```java
// Delete: StrutsTestCase-based tests
// Add: Spring Boot Test-based tests
@SpringBootTest
@AutoConfigureMockMvc
class MyControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testSomething() throws Exception {
        mockMvc.perform(get("/path"))
            .andExpect(status().isOk());
    }
}
```

##### Problem 3: Screen does not display

**Cause**: After JSP deletion, Thymeleaf template path is incorrect

**Solution**:

```yaml
# Verify in application.yml
spring:
  thymeleaf:
    prefix: classpath:/templates/  # Correct path
    suffix: .html
```

```java
// Return correct view name in Controller
@GetMapping("/products")
public String listProducts() {
    return "products/list";  // templates/products/list.html
}
```

## Major Migration Challenges and Solutions

### 1. Business Logic Extraction

**Challenge**: Business logic is often written directly in Struts Action

**Solution**:

- Gradually extract logic from Action to Service layer
- Set transaction boundaries appropriately (`@Transactional`)
- Improve testability by utilizing dependency injection

### 2. Session Management

**Challenge**: Struts 1.x directly manipulates HttpSession

**Solutions**:

- Use Spring Session (optional)
- Utilize session-scoped Beans
- Recommend stateless design (RESTful)

### 3. Database Schema

**Challenge**: Consistency with existing database schema

**Solutions**:

- Align JPA entities with existing table structure
- Specify existing table name with `@Table(name="existing_table")`
- Manage migrations with Flyway or Liquibase as needed

## Risk Assessment and Mitigation

| Risk | Severity | Probability | Impact | Mitigation |
| --- | --- | --- | --- | --- |
| Insufficient understanding of business logic | High | Medium | Incorrect feature implementation | Documentation, interviews with original developers |
| Database schema inconsistency | High | Low | Data corruption | Complete backup before migration, gradual release |
| Performance degradation | Medium | Low | Poor user experience | Performance testing, profiling |
| Undetected bugs | Medium | Medium | Production failures | Sufficient test coverage, gradual release |
| Learning cost | Medium | High | Schedule delays | Training sessions, pair programming |
| External library compatibility | Low | Low | Build errors | Preliminary research, alternative library evaluation |

## Timeline and Effort Estimation

| Phase | Duration | Required Resources | Deliverables |
| --- | --- | --- | --- |
| Phase 0: Preparation | 1 week | 1-2 people | Environment setup, current state analysis document |
| Phase 1: Project structure | 1 week | 2 people | Spring Boot project, basic configuration |
| Phase 2: Data access layer | 2 weeks | 2-3 people | Entities, repositories, services |
| Phase 3: Controller layer | 3 weeks | 3-4 people | All Controllers, DTOs, validation |
| Phase 4: View layer | 3 weeks | 2-3 people | All Thymeleaf templates |
| Phase 5: Modern Java refactoring | 1 week | 2 people | Java 25 modern code applied |
| Phase 6: Configuration and other | 1 week | 2 people | Exception handling, file upload, etc. |
| Phase 7: Testing | 2 weeks | 3-4 people | Unit and integration tests |
| Phase 8: Performance testing | 1 week | 2 people | Performance measurement, tuning |
| Phase 9: Documentation | 1 week | 1-2 people | Technical documents, operations manual |
| Phase 10: Legacy code cleanup | 1 week | 2-3 people | Clean codebase, final verification |
| **Total** | **Approximately 17 weeks (4.25 months)** | **2-4 people** | |

### Parallel Work Possibilities

- Phase 3 and Phase 4 can be partially executed in parallel
- Tests can be created in parallel with each phase
- Phase 10 is executed after all features are migrated (no parallel work)

## Gradual Release Strategy

### Strangler Pattern (Recommended)

Run existing Struts 1.x application and Spring Boot application in parallel:

1. **Phase 1**: Develop new features in Spring Boot
2. **Phase 2**: Migrate low-frequency screens first
3. **Phase 3**: Migrate core features
4. **Phase 4**: After all features are migrated, decommission Struts 1.x version

**Benefits**:

- Risk distribution
- Gradual verification
- Easy rollback

**Implementation**:

- Route by URL path using reverse proxy (Nginx, etc.)
- `/api/*` → Spring Boot
- Other → Struts 1.x

### Big Bang Migration

Migrate all features at once:

**Benefits**:

- No dual management required
- Shorter migration period

**Drawbacks**:

- Higher risk
- Difficult rollback

**Recommendation**: Only for small-scale applications

## Migration Checklist

### Preparation Phase

- [ ] Form project team
- [ ] Obtain stakeholder approval
- [ ] Install and configure JDK 25
- [ ] Generate project with Spring Initializr
- [ ] Decide Git repository branching strategy
- [ ] Prepare CI/CD pipeline

### Data Access Layer

- [ ] Analyze database schema
- [ ] Create JPA entity classes
- [ ] Create Spring Data JPA repositories
- [ ] Create service layer
- [ ] Set transaction boundaries
- [ ] Unit tests for repositories and services

### Controller Layer

- [ ] Inventory Struts Actions
- [ ] Convert to Spring MVC Controllers
- [ ] Create DTO classes
- [ ] Implement Bean Validation
- [ ] Implement exception handling
- [ ] Unit tests for Controllers

### View Layer

- [ ] Inventory JSP pages
- [ ] Convert to Thymeleaf templates
- [ ] Create layout templates
- [ ] Migrate CSS/JavaScript
- [ ] Verify message resources
- [ ] Verify screen display operation

### Other Features

- [ ] Migrate file upload functionality
- [ ] Migrate email sending functionality
- [ ] Implement session management
- [ ] Security configuration (if needed)
- [ ] Verify log configuration

### Modern Java Refactoring

- [ ] Apply try-with-resources
- [ ] Apply diamond operator and `var`
- [ ] Apply pattern matching for `instanceof`/`switch`
- [ ] Apply text blocks (SQL/JSON/HTML)
- [ ] Migrate to `java.time`
- [ ] Migrate to Stream/Optional (where appropriate)
- [ ] Apply `record`/DTO (exclude JPA entities)
- [ ] Consider `HttpClient`/virtual threads

### Testing and Deployment

- [ ] Complete unit tests
- [ ] Complete integration tests
- [ ] Performance testing
- [ ] Security testing
- [ ] Deploy to staging environment
- [ ] Deploy to production environment

### Legacy Code Cleanup

- [ ] Confirm completion of all Spring Boot feature migration
- [ ] Delete Struts-related configuration files
- [ ] Delete Struts Action and ActionForm classes
- [ ] Delete all JSP files
- [ ] Remove unnecessary dependencies from pom.xml
- [ ] Clean up web.xml
- [ ] Verify build after cleanup
- [ ] Test all features after cleanup
- [ ] Performance test after cleanup
- [ ] Final code review

## Next Steps

### Immediate Actions

1. **Stakeholder Meeting**
   - Explain and approve migration plan
   - Decide resource allocation
   - Agree on release schedule

2. **Technical Evaluation**
   - Conduct POC (proof of concept)
   - Implement one key feature in Spring Boot
   - Verify performance

3. **Team Preparation**
   - Conduct Spring Boot training
   - Learn Thymeleaf and Spring Data JPA
   - Establish pair programming system

### Within 1 Month

1. **Environment Setup**
   - Organize development environment
   - Build CI/CD pipeline
   - Prepare test environment

2. **Complete Phase 0-1**
   - Create current state analysis document
   - Create Spring Boot project
   - Complete basic configuration

### Within 3 Months

1. **Core Feature Migration**
   - Complete data access layer migration
   - Migrate main screen controllers and views
   - Complete basic tests

2. **Staging Environment Deployment**
   - Test migrated features in staging environment
   - Collect feedback and improve

### Within 6 Months

1. **Complete All Feature Migration**
   - Complete Spring Boot conversion of all Struts features
   - Complete comprehensive testing
   - Organize documentation

2. **Production Environment Deployment**
   - Gradual release or all-at-once switchover
   - Establish monitoring system
   - Gradual decommissioning of old system

## Success Factors

### Technical Aspects

1. **Gradual Migration**: Don't change everything at once
2. **Sufficient Testing**: Test thoroughly at each phase
3. **Continuous Integration**: Automated testing and builds
4. **Performance Monitoring**: Measure performance before and after migration
5. **Apply Modern Java**: try-with-resources, `java.time`, Streams, pattern matching, records

### Organizational Aspects

1. **Management Commitment**: Secure resources and schedule
2. **Team Skill Development**: Continuous learning and training
3. **Clear Communication**: Transparent progress reporting
4. **Appropriate Risk Management**: Early problem detection and response

## Expected Effects

### Short-term Effects (Within 6 Months)

- Significant reduction of security risks
- Improved development productivity (auto-configuration, hot reload, etc.)
- Improved maintainability

### Medium to Long-term Effects (After 6 Months)

- Improved code quality through use of Java 25 latest features
- Path to microservices
- Possibility of migrating to cloud-native architecture
- Easier new developer onboarding
- Enhanced community support

## Summary

This project uses Apache Struts 1.3.10, a framework from 2008, and is in a state of extremely high security risk. This migration plan proposes a complete migration to **Java 25 + Spring Boot 3.5.x + Thymeleaf + Spring Data JPA**.

### Migration Benefits

1. **Security**: From EOL Struts 1.x to continuously supported Spring Boot
2. **Productivity**: Auto-configuration, development tools, rich ecosystem
3. **Maintainability**: Modern architecture, clear separation of concerns
4. **Future-proofing**: Migration path to microservices and cloud-native
5. **Talent**: Abundance of Spring developers, rich learning resources

### Recommended Implementation Approach

**Duration**: Approximately 6 months (17 weeks development + testing, deployment, cleanup)

**Resources**: 2-4 developers

**Release Strategy**: Gradual migration using Strangler Pattern (recommended)

**Final Phase**: Apply modern Java in Phase 5, completely remove legacy code in Phase 10

### Return on Investment

| Item | Short-term (6 months) | Medium-term (1-2 years) | Long-term (2+ years) |
| --- | --- | --- | --- |
| Development Cost | High (migration work) | Low (improved productivity) | Low (easy maintenance) |
| Security Risk | Significantly reduced | Minimized | Minimized |
| Development Speed | Temporary slowdown | Improved | Greatly improved |
| System Quality | Improved | Greatly improved | Greatly improved |

### Final Recommendations

This Apache Struts 1.x application is in a state where **migration should begin immediately**. Considering security risks and technical debt, **complete migration to Spring Boot 3.5.x is the optimal choice**.

- ✅ **Java 25**: LTS support until 2031
- ✅ **Spring Boot 3.5.x**: Industry standard, rich ecosystem
- ✅ **Thymeleaf**: Modern, maintainable template engine
- ✅ **Spring Data JPA**: Declarative, highly productive data access

**3 Actions to Start Immediately:**

1. Explain to stakeholders and obtain approval
2. Conduct technical POC (1-2 weeks)
3. Form migration team and begin training

### Final Work After Migration Completion

After migration is complete and confirming the Spring Boot application operates normally, **implement legacy code cleanup in Phase 10**. This will:

- Completely delete old Struts code and JSP files
- Remove unnecessary dependencies to reduce application size
- Keep codebase clean and greatly improve maintainability
- Completely eliminate technical debt

Through this migration, you can build a secure, maintainable, and extensible application for the future.
