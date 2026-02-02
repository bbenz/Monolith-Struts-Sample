# Spring Boot Migration Plan

## Migration from Apache Struts 1.x to Spring Boot 3.x + Thymeleaf + Spring Data JPA

## Overview

This project uses Apache Struts 1.3.10, an old Java framework, targeting Java 1.5. This document outlines a complete migration plan to **Java 21 + Spring Boot 3.2.x + Thymeleaf + Spring Data JPA**.

## Current State

### Java Version

- **Current**: Java 1.5 (released 2004, end of support)
- **Migration target**: Java 21 LTS (released September 2023, LTS support until September 2031)

### Framework

- **Current**: Apache Struts 1.3.10 (released 2008, EOL, numerous known vulnerabilities)
- **Migration target**: Spring Boot 3.2.x (latest stable, long-term support)

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
| | commons-dbutils | 1.1 | 1.8.1 | Upgrade possible |
| | PostgreSQL JDBC | 9.2-1004-jdbc3 | 42.7.4 | Major upgrade |
| **File Upload** | | | | |
| | commons-fileupload | 1.3.3 | 1.5 | Security fixes available |
| **Logging** | | | | |
| | log4j | 1.2.17 | N/A | Migration to Log4j2 2.23.1 recommended |
| **Email** | | | | |
| | javax.mail | 1.4.7 | Jakarta Mail 2.1.3 | Migration to Jakarta EE |
| **View Template/Web** | | | | |
| | jsp-api | 2.1 | Thymeleaf 3.1.x | Migration from JSP to Thymeleaf |
| | servlet-api | 2.5 | Spring Boot embedded (Tomcat 10.1.x) | Included in Spring Boot Starter |
| | - | - | Spring Web MVC 6.1.x | RESTful Web Service support |
| **Testing** | | | | |
| | JUnit | 4.12 | JUnit 5.10.2 | Migration to JUnit Jupiter |
| | H2 Database | 1.3.176 | 2.2.224 | For testing |
| | StrutsTestCase | 2.1.4-1.2-2.4 | N/A | Struts dependency, consider removal |

## Migration Strategy

### Why Choose Spring Boot

**Apache Struts 1.x reached EOL in 2013 and has numerous known vulnerabilities.** Partial dependency upgrades will not solve fundamental issues.

#### Reasons to Recommend Complete Migration to Spring Boot 3.2.x

1. **Security**: Continuous security updates and support
2. **Community**: Largest Java community with abundant documentation
3. **Modern Technology**: Can leverage all Java 21 features
4. **Productivity**: High-speed development with auto-configuration, embedded server, development tools
5. **Future-proofing**: Clear migration path to microservices and cloud-native
6. **Ecosystem**: Rich Spring Boot starters, integration support

### Target Technology Stack

| Component | Struts 1.x | Spring Boot 3.2.x |
| --- | --- | --- |
| **Framework** | Apache Struts 1.3.10 | Spring Boot 3.2.x + Spring MVC 6.1.x |
| **Java Version** | Java 1.5 | Java 21 LTS |
| **View Template** | JSP + Struts Taglib | Thymeleaf 3.1.x |
| **Data Access** | JDBC + Commons DBUtils | Spring Data JPA 3.2.x + Hibernate 6.4.x |
| **Connection Pool** | Commons DBCP 1.x | HikariCP (Spring Boot standard) |
| **Validation** | Commons Validator | Bean Validation 3.0 (Hibernate Validator) |
| **Logging** | Log4j 1.2.17 | Logback (Spring Boot standard) + SLF4J |
| **Dependency Injection** | None | Spring IoC Container |
| **Testing** | JUnit 4 + StrutsTestCase | JUnit 5 + Spring Boot Test |
| **Build Tool** | Maven 2.x series | Maven 3.9.x |
| **Application Server** | Tomcat 6/7 (external) | Embedded Tomcat 10.1.x |

## Struts 1.x to Spring Boot Correspondence

### Architecture Correspondence

| Struts 1.x Component | Spring Boot Correspondence | Description |
| --- | --- | --- |
| **Action** | `@Controller` + `@RequestMapping` | Request processing |
| **ActionForm** | `@ModelAttribute` + Bean Validation | Form data binding |
| **struts-config.xml** | Java Config (`@Configuration`) | Application configuration |
| **ActionForward** | `ModelAndView` / `return "viewName"` | View transition |
| **ActionMapping** | `@RequestMapping` / `@GetMapping` / `@PostMapping` | URL mapping |
| **ActionServlet** | `DispatcherServlet` (auto-configured) | Front controller |
| **JSP + Struts Tags** | Thymeleaf templates | View rendering |
| **Validator Framework** | Bean Validation + `@Valid` | Input validation |
| **MessageResources** | `MessageSource` + `messages.properties` | Internationalization |
| **DAO (manual JDBC)** | Spring Data JPA Repository | Data access |
| **DataSource (DBCP)** | HikariCP (auto-configured) | Connection pool |

## Phased Migration Plan

### Phase 0: Preparation Phase (1 week)

#### Task Content

1. **Current State Analysis**
   - Create list of all Action classes
   - Create list of all JSP pages
   - Investigate database access patterns
   - Confirm external library dependencies

2. **Environment Setup**
   - Install JDK 21
   - Prepare IDE (IntelliJ IDEA / Eclipse)
   - Determine Git branch strategy (e.g., `feature/spring-boot-migration`)

3. **Create Spring Boot Project**
   - Generate basic project with Spring Initializr
   - Add necessary dependencies

#### Spring Initializr Configuration

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

#### Generated pom.xml Example

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
        <version>3.2.2</version>
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
  
  # Datasource configuration
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
    cache: false  # false for development
    prefix: classpath:/templates/
    suffix: .html
    mode: HTML
  
  # Email configuration
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
├── dto/                # Data transfer objects
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
    
    // Auto-generate query from method name
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

#### Migrating from Struts Action to Spring MVC Controller

**Spring Boot Controller Example:**

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
        
        redirectAttributes.addFlashAttribute("message", "Product registered");
        return "redirect:/products";
    }
}
```

#### Create DTO

```java
package com.skishop.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductFormDTO {
    
    private Long id;
    
    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name must be within 100 characters")
    private String name;
    
    @Size(max = 500, message = "Description must be within 500 characters")
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

#### Struts Taglib to Thymeleaf Correspondence Table

| Struts 1.x Tag | Thymeleaf Equivalent | Description |
| --- | --- | --- |
| `<bean:write name="var"/>` | `th:text="${var}"` | Variable output |
| `<bean:message key="key"/>` | `th:text="#{key}"` | Message resource |
| `<html:link action="/path">` | `th:href="@{/path}"` | Link |
| `<html:form action="/submit">` | `th:action="@{/submit}" method="post"` | Form |
| `<html:text property="name"/>` | `th:field="*{name}"` | Text input |
| `<html:errors property="name"/>` | `th:errors="*{name}"` | Validation errors |
| `<logic:iterate id="item" name="list">` | `th:each="item : ${list}"` | Loop |
| `<logic:present name="var">` | `th:if="${var != null}"` | Existence check |
| `<logic:notPresent name="var">` | `th:if="${var == null}"` | Non-existence check |
| `<logic:equal name="var" value="val">` | `th:if="${var == 'val'}"` | Value comparison |

### Phase 5: Modern Java Refactoring (1 week)

#### Objective

- Modernize Java 5-era code style and APIs to Java 21 modern writing style, improving readability, safety, and performance.

#### Checkpoints

| Item | Before Example | After Example | Notes |
| --- | --- | --- | --- |
| Resource release | `try { ... } finally { close(); }` | `try (var in = ...) { ... }` | try-with-resources |
| Type inference | `Map<String, List<String>> map = new HashMap<String, List<String>>();` | `var map = new HashMap<String, List<String>>();` | diamond + var |
| instanceof | `if (obj instanceof Foo) { Foo f = (Foo) obj; }` | `if (obj instanceof Foo f) { ... }` | pattern matching |
| Strings | `"line1\nline2"` | `"""line1\nline2"""` | text blocks |
| Date/time | `Date/Calendar` | `LocalDateTime/Instant` | java.time |
| Collections | `new ArrayList<>()` then add | `List.of(...)` | Immutable list |
| Collection factory | `new HashSet<>(); add...` | `Set.of(...) / Map.of(...)` | Immutable Set/Map |
| Map initialization | `if (!map.containsKey(k)) map.put(k, ...)` | `map.computeIfAbsent(k, ...)` | Java 8 |
| Loop | `for (String s : list) { ... }` | `list.stream().map(...).toList()` | Streams (appropriate use) |
| DTO | `class Foo { ... }` | `record Foo(...) {}` | DTO/Value Object only |
| HTTP | `HttpURLConnection` | `HttpClient` | Java 11 |
| Concurrency | `ExecutorService` | `virtual threads (Threads.ofVirtual().factory())` | Consider carefully |
| Lambda/method reference | `new Runnable(){ public void run(){...}}` | `Runnable r = () -> {...}` / `System.out::println` | Java 8 |
| Functional IF | Many custom IFs | `java.util.function.*` | Reuse and unification |
| switch syntax | `switch(x){case A: ... break;}` | `switch (x) { case A -> ...; default -> ...; }` | switch expressions |
| switch pattern | Type branching with `if/else` | `switch (obj) { case String s -> ... }` | Java 21 |
| Multi-catch | Multiple duplicate catches | `catch (IOException\|SQLException e)` | Java 7 |
| Numeric literals | `1000000` | `1_000_000` | Readability |
| Optional | `if (obj == null) ...` | `Optional.ofNullable(obj).ifPresent(...)` | null safety |
| Stream extension | `collect(Collectors.toList())` | `.toList()` | Java 16 |
| Stream extension2 | Procedural for loop | `stream().takeWhile(...).dropWhile(...)` | Java 9 |
| Optional extension | `if (obj == null) ...` | `opt.ifPresentOrElse(...); opt.orElseThrow(); opt.stream()` | Java 9/10 |
| NIO.2 | `new File(...)` | `Path/Files.walk(...)` | Java 7 |
| CompletableFuture | `Future` + `get()` | `CompletableFuture.supplyAsync(...)` | Asynchronous processing |
| Sealed | Control with `abstract class` | `sealed interface Shape permits Circle, Square {}` | Java 17 |
| String utility | `trim().isEmpty()` | `isBlank()/strip()/lines()/repeat()` | Java 11 |
| Random | `new Random()` | `ThreadLocalRandom.current()` / `RandomGenerator` | Thread-safe/reproducible |
| finalize | `protected void finalize()` | `Cleaner` / try-with-resources | Java 9+ deprecated |
| Record pattern | `if (obj instanceof Point) { ... }` | `if (obj instanceof Point(int x, int y)) { ... }` | Java 21 |
| String template | `"Hello " + name` | `STR."Hello ${name}"` | Java 21 (Preview) |

#### Implementation Steps

1. Detect candidates with static analysis (IDE inspections, SpotBugs, Checkstyle, SonarLint)
2. Apply IDE refactoring + auto-fixes (try-with-resources, diamond, var, pattern matching, text blocks)
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

### Phase 7: Test Creation (2 weeks)

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

#### Performance Testing Implementation

##### Load Testing Using JMeter

```xml
<!-- Add to pom.xml -->
<dependency>
    <groupId>org.apache.jmeter</groupId>
    <artifactId>ApacheJMeter_core</artifactId>
    <version>5.6.3</version>
    <scope>test</scope>
</dependency>
```

##### Application Metrics Monitoring

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
   - 100 users simultaneous access
   - Response time < 500ms
   - Error rate < 1%

2. **Database Query Optimization**
   - Detect and fix N+1 problem
   - Index optimization
   - Query plan analysis

3. **Caching Strategy**

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

- [ ] Optimize database connection pool configuration (HikariCP)
- [ ] Optimize JPA/Hibernate queries (Lazy Loading, Eager Loading)
- [ ] Create appropriate indexes
- [ ] Implement caching strategy
- [ ] Reduce unnecessary log output
- [ ] Compress and cache static resources
- [ ] Adjust JVM heap size
- [ ] Optimize garbage collection

### Phase 9: Documentation and Operations Preparation (1 week)

#### Documents to Create

##### 1. Architecture Documentation

**Content:**

- System architecture diagram
- Inter-component dependencies
- Data flow diagram
- Deployment architecture

##### 2. API Specification

```java
// Automated API documentation using SpringDoc
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

**Content:**

- Application start/stop procedure
- How to check logs
- Troubleshooting guide
- Backup/restore procedure
- Deployment procedure

**Startup Command Examples:**

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
- Coding conventions
- How to write tests
- Local development environment setup
- Common problems and solutions

##### 5. Migration Report

**Content:**

- Before/after comparison
- Problems encountered and solutions
- Remaining technical debt
- Future improvement suggestions

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

#### CI/CD Pipeline (GitHub Actions Example)

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
    
    - name: Set up JDK 21
      uses: actions/setup-java@v4
      with:
        java-version: '21'
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

### Phase 10: Legacy Code Cleanup and Final Validation (1 week)

#### Cleanup Objective

After completing migration through Phase 8 and confirming that the Spring Boot application is working properly, remove old Struts-related code, configuration files, and JSP-related code that are no longer used. This organizes the codebase and improves maintainability.

#### Files and Code to Remove

##### 1. Struts-related Configuration Files

```text
Files to remove:
- WEB-INF/struts-config.xml
- WEB-INF/validation.xml
- WEB-INF/validator-rules.xml
- WEB-INF/tiles-defs.xml
- src/main/resources/struts.properties
- src/main/resources/validation.properties
```

##### 2. Struts Action Classes

```text
Directories to remove:
- src/main/java/com/*/action/
- src/main/java/com/*/struts/

Confirmation items:
- All Action classes have been migrated to Spring MVC Controllers
- Business logic has been extracted to Service layer
```

##### 3. ActionForm Classes

```text
Files to remove:
- src/main/java/com/*/form/
- *ActionForm.java

Confirmation items:
- All form classes have been migrated to DTOs
- Bean Validation annotations have been applied
```

##### 4. JSP Files and Struts Taglibs

```text
Files to remove:
- src/main/webapp/**/*.jsp
- src/main/webapp/WEB-INF/tags/
- WEB-INF/tld/*.tld (Struts Tag Library definitions)

Confirmation items:
- All JSPs have been migrated to Thymeleaf templates
- Screen display operation verification has been completed
```

##### 5. Struts-related Dependencies (pom.xml)

```xml
Dependencies to remove:
<dependencies>
    <!-- Remove: Apache Struts -->
    <dependency>
        <groupId>struts</groupId>
        <artifactId>struts</artifactId>
        <version>1.3.10</version>
    </dependency>
    
    <!-- Remove: Commons Validator -->
    <dependency>
        <groupId>commons-validator</groupId>
        <artifactId>commons-validator</artifactId>
        <version>1.3.1</version>
    </dependency>
    
    <!-- Remove: Commons Digester -->
    <dependency>
        <groupId>commons-digester</groupId>
        <artifactId>commons-digester</artifactId>
        <version>1.8</version>
    </dependency>
    
    <!-- Remove: Commons BeanUtils -->
    <dependency>
        <groupId>commons-beanutils</groupId>
        <artifactId>commons-beanutils</artifactId>
        <version>1.8.0</version>
    </dependency>
    
    <!-- Remove: Commons Chain -->
    <dependency>
        <groupId>commons-chain</groupId>
        <artifactId>commons-chain</artifactId>
        <version>1.2</version>
    </dependency>
    
    <!-- Remove: Servlet API (included in Spring Boot) -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>servlet-api</artifactId>
        <version>2.5</version>
    </dependency>
    
    <!-- Remove: JSP API (migrated to Thymeleaf) -->
    <dependency>
        <groupId>javax.servlet.jsp</groupId>
        <artifactId>jsp-api</artifactId>
        <version>2.1</version>
    </dependency>
    
    <!-- Remove: JSTL (migrated to Thymeleaf) -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>jstl</artifactId>
        <version>1.2</version>
    </dependency>
    
    <!-- Remove: StrutsTestCase -->
    <dependency>
        <groupId>strutstestcase</groupId>
        <artifactId>strutstestcase</artifactId>
        <version>2.1.4-1.2-2.4</version>
    </dependency>
</dependencies>
```

##### 6. web.xml Updates

```xml
Remove the following web.xml content:
<!-- Remove: Struts ActionServlet configuration -->
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

<!-- Remove: Struts TagLib configuration -->
<jsp-config>
    <taglib>
        <taglib-uri>/tags/struts-bean</taglib-uri>
        <taglib-location>/WEB-INF/struts-bean.tld</taglib-location>
    </taglib>
    <!-- Other Struts taglib definitions -->
</jsp-config>

Note: web.xml is usually not needed in Spring Boot, but
      if you need to keep existing configuration,
      remove only Struts-related configuration.
```

##### 7. Other Configuration Files

```text
Files to remove:
- src/main/resources/ApplicationResources.properties (already migrated to messages.properties)
- src/main/webapp/WEB-INF/classes/ (unnecessary class files)
```

#### Cleanup Procedure

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
# Verify all functionality works
```

##### Step 2: Remove Struts-related Files (2 days)

```bash
# Remove Struts configuration files
rm -f src/main/webapp/WEB-INF/struts-config.xml
rm -f src/main/webapp/WEB-INF/validation.xml
rm -f src/main/webapp/WEB-INF/validator-rules.xml
rm -f src/main/webapp/WEB-INF/tiles-defs.xml
rm -rf src/main/webapp/WEB-INF/tld/

# Remove Struts Java code
find src/main/java -type d -name "action" -exec rm -rf {} +
find src/main/java -type d -name "form" -exec rm -rf {} +
find src/main/java -name "*Action.java" -delete
find src/main/java -name "*ActionForm.java" -delete

# After each removal, always run build and tests
mvn clean compile
mvn test
```

##### Step 3: Remove JSP-related Files (2 days)

```bash
# Verify Thymeleaf templates exist before removing all JSP files
find src/main/resources/templates -name "*.html" | wc -l

# Remove JSP files
rm -rf src/main/webapp/*.jsp
rm -rf src/main/webapp/WEB-INF/jsp/
rm -rf src/main/webapp/WEB-INF/pages/

# Remove tag files
rm -rf src/main/webapp/WEB-INF/tags/

# Verify operation after each removal
mvn spring-boot:run
# Verify all screens in browser
```

##### Step 4: Clean up pom.xml (1 day)

```bash
# Remove unnecessary dependencies from pom.xml
# Edit manually, or verify with the following command

# Detect unused dependencies
mvn dependency:analyze

# Verify build and tests have no issues
mvn clean install
mvn test

# Verify dependency tree
mvn dependency:tree
```

##### Step 5: Update or Remove web.xml (1 day)

```bash
# web.xml is basically unnecessary in Spring Boot
# If web.xml is empty after removing Struts configuration, remove it
rm -f src/main/webapp/WEB-INF/web.xml

# Or update to keep only necessary configuration
# (if there are configurations needed after Spring Boot migration, like Filter settings)
```

#### Post-Cleanup Validation Checklist

##### 1. Build and Test Validation

- [ ] `mvn clean compile` succeeds
- [ ] All tests pass with `mvn test`
- [ ] WAR/JAR file is generated normally with `mvn package`
- [ ] No compilation errors at all
- [ ] Check and address warning messages

##### 2. Application Startup Validation

- [ ] Application starts normally with `mvn spring-boot:run`
- [ ] No errors in startup logs
- [ ] Spring Boot banner is displayed
- [ ] All Beans are loaded normally
- [ ] Database connection is established

##### 3. Functional Test Validation

- [ ] All screens display normally (Thymeleaf templates)
- [ ] All form submissions work normally
- [ ] Database registration/update/deletion works normally
- [ ] File upload functionality works (if applicable)
- [ ] Email sending functionality works (if applicable)
- [ ] Session management functions normally
- [ ] Error handling works normally

##### 4. Performance Test Validation

- [ ] Response time has not degraded
- [ ] Memory usage is appropriate
- [ ] CPU usage is within normal range
- [ ] Database connection pool works normally

##### 5. Security Test Validation

- [ ] Authentication/authorization works normally
- [ ] XSS countermeasures function (Thymeleaf auto-escaping)
- [ ] CSRF countermeasures function (if needed)
- [ ] SQL injection countermeasures function (using JPA)

##### 6. Codebase Validation

- [ ] No Struts-related import statements remain
- [ ] No unused classes exist
- [ ] Check and address TODO/FIXME comments
- [ ] Code static analysis (SonarQube, etc.)

```bash
# Verify no references to Struts
grep -r "import org.apache.struts" src/
grep -r "struts" pom.xml

# Verify no JSP-related references
grep -r "import javax.servlet.jsp" src/
grep -r "jsp-api" pom.xml

# Verify search results are empty
```

#### Final Processing After Cleanup

##### 1. Update Documentation

```markdown
Documents to update:
- README.md (update startup method, technology stack)
- CHANGELOG.md (record migration history)
- API specification (OpenAPI/Swagger)
- Operations manual (update deployment procedure)
```

##### 2. Commit Changes and Pull Request

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

##### 3. Final Check Before Production Deployment

- [ ] Full testing in staging environment
- [ ] Execute load testing
- [ ] Execute security scanning
- [ ] Demo to stakeholders
- [ ] Final confirmation of deployment plan
- [ ] Prepare rollback procedure

##### 4. Production Environment Deployment

```bash
# Create tag
git tag -a v2.0.0 -m "Spring Boot migration completed - Legacy code removed"
git push origin v2.0.0

# Production deployment (via CI/CD pipeline)
# or manual deployment
```

#### Effects of Cleanup

##### Quantitative Effects

| Item | Reduction Amount (Estimated) | Notes |
| --- | --- | --- |
| Lines of code | 30-50% reduction | Removal of Action, ActionForm, JSP |
| Number of dependency libraries | 10-15 reduction | Removal of Struts-related libraries |
| WAR file size | 20-30% reduction | Removal of unnecessary libraries and JSP |
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
   - Removal of EOL framework
   - Removal of legacy code
   - Investment in the future

#### Troubleshooting

##### Problem 1: Build Errors After Removal

**Cause**: Some code depends on removed classes

**Solution**:

```bash
# Identify dependent classes from error log
mvn clean compile 2>&1 | grep "cannot find symbol"

# Fix the location
# - Replace with Spring Boot equivalent functionality
# - Remove if unnecessary code
```

##### Problem 2: Tests Fail

**Cause**: Test code depends on StrutsTestCase

**Solution**:

```java
// Remove: StrutsTestCase-based tests
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

##### Problem 3: Screen Not Displayed

**Cause**: After JSP removal, Thymeleaf template path is incorrect

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

## Main Migration Challenges and Countermeasures

### 1. Business Logic Extraction

**Challenge**: Business logic is often written directly in Struts Actions

**Countermeasures**:

- Gradually extract logic from Actions to Service layer
- Set appropriate transaction boundaries (`@Transactional`)
- Improve testability using dependency injection

### 2. Session Management

**Challenge**: Struts 1.x directly manipulates HttpSession

**Countermeasures**:

- Use Spring Session (optional)
- Utilize session scope Beans
- Recommend stateless design (RESTful)

### 3. Database Schema

**Challenge**: Consistency with existing database schema

**Countermeasures**:

- Align JPA entities with existing table structure
- Specify existing table name with `@Table(name="existing_table")`
- Use Flyway or Liquibase for migration management if needed

## Risk Assessment and Mitigation

| Risk | Severity | Probability | Impact | Mitigation |
| --- | --- | --- | --- | --- |
| Insufficient understanding of business logic | High | Medium | Incorrect functionality implementation | Documentation, interviews with original developers |
| Database schema inconsistency | High | Low | Data corruption | Complete backup before migration, phased release |
| Performance degradation | Medium | Low | Degraded user experience | Conduct performance tests, profiling |
| Undetected bugs | Medium | Medium | Production failures | Sufficient test coverage, phased release |
| Learning costs | Medium | High | Schedule delays | Conduct training, pair programming |
| External library compatibility | Low | Low | Build errors | Preliminary research, consider alternative libraries |

## Timeline and Work Estimation

| Phase | Duration | Required Resources | Deliverables |
| --- | --- | --- | --- |
| Phase 0: Preparation | 1 week | 1-2 people | Environment setup, current state analysis document |
| Phase 1: Project structure | 1 week | 2 people | Spring Boot project, basic configuration |
| Phase 2: Data access layer | 2 weeks | 2-3 people | Entities, repositories, services |
| Phase 3: Controller layer | 3 weeks | 3-4 people | All Controllers, DTOs, validation |
| Phase 4: View layer | 3 weeks | 2-3 people | All Thymeleaf templates |
| Phase 5: Modern Java refactoring | 1 week | 2 people | Apply Java 21 modern code |
| Phase 6: Configuration/other | 1 week | 2 people | Exception handling, file upload, etc. |
| Phase 7: Testing | 2 weeks | 3-4 people | Unit/integration tests |
| Phase 8: Performance testing | 1 week | 2 people | Performance measurement, tuning |
| Phase 9: Documentation | 1 week | 1-2 people | Technical documentation, operations manual |
| Phase 10: Legacy code cleanup | 1 week | 2-3 people | Clean codebase, final validation |
| **Total** | **~17 weeks (4.25 months)** | **2-4 people** | |

### Possibility of Parallel Work

- Phases 3 and 4 can be partially executed in parallel
- Tests can be created in parallel with each phase
- Phase 10 is executed after all functionality migration is complete (no parallel work)

## Phased Release Strategy

### Strangler Pattern (Recommended)

Run existing Struts 1.x application and Spring Boot application in parallel:

1. **Phase 1**: Develop new features with Spring Boot
2. **Phase 2**: Migrate from low-frequency screens
3. **Phase 3**: Migrate main features
4. **Phase 4**: After all functionality migration is complete, discontinue Struts 1.x version

**Benefits**:

- Risk distribution
- Gradual verification
- Easy rollback

**Implementation Method**:

- Route based on URL path using reverse proxy (Nginx, etc.)
- `/api/*` → Spring Boot
- Other → Struts 1.x

### Big Bang Migration

Migrate all functionality at once:

**Benefits**:

- No dual management needed
- Short migration period

**Disadvantages**:

- High risk
- Difficult rollback

**Recommendation**: Small applications only

## Migration Checklist

### Preparation Phase

- [ ] Form project team
- [ ] Obtain stakeholder approval
- [ ] Install and configure JDK 21 environment
- [ ] Generate project with Spring Initializr
- [ ] Determine Git repository branch strategy
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
- [ ] Verify logging configuration

### Modern Java Refactoring

- [ ] Apply try-with-resources
- [ ] Apply diamond operator/`var`
- [ ] Apply `instanceof`/`switch` pattern matching
- [ ] Apply text blocks (SQL/JSON/HTML)
- [ ] Migrate to `java.time`
- [ ] Migrate to Stream/Optional (appropriate use)
- [ ] Apply `record`/DTO (exclude JPA entities)
- [ ] Consider `HttpClient`/virtual threads

### Testing and Deployment

- [ ] Complete unit testing
- [ ] Complete integration testing
- [ ] Performance testing
- [ ] Security testing
- [ ] Deploy to staging environment
- [ ] Deploy to production environment

### Legacy Code Cleanup

- [ ] Confirm all functionality Spring Boot migration complete
- [ ] Remove Struts-related configuration files
- [ ] Remove Struts Action and ActionForm classes
- [ ] Remove all JSP files
- [ ] Remove unnecessary dependencies from pom.xml
- [ ] Clean up web.xml
- [ ] Verify build after cleanup
- [ ] Test all functionality after cleanup
- [ ] Performance test after cleanup
- [ ] Final code review

## Next Steps

### Actions to Take Immediately

1. **Stakeholder Meeting**
   - Explain and approve migration plan
   - Decide resource allocation
   - Agree on release schedule

2. **Technical Assessment**
   - Execute POC (Proof of Concept)
   - Implement one main feature with Spring Boot
   - Verify performance

3. **Team Preparation**
   - Conduct Spring Boot training
   - Learn Thymeleaf and Spring Data JPA
   - Establish pair programming system

### Within 1 Month

1. **Environment Setup**
   - Prepare development environment
   - Build CI/CD pipeline
   - Prepare test environment

2. **Complete Phases 0-1**
   - Create current state analysis document
   - Create Spring Boot project
   - Complete basic configuration

### Within 3 Months

1. **Core Feature Migration**
   - Complete data access layer migration
   - Migrate main screen controllers and views
   - Complete basic testing

2. **Staging Environment Deployment**
   - Test migrated features in staging environment
   - Collect and improve feedback

### Within 6 Months

1. **Complete All Feature Migration**
   - Spring Boot-ify all Struts features
   - Complete comprehensive testing
   - Organize documentation

2. **Production Environment Deployment**
   - Phased release or complete switchover
   - Establish monitoring system
   - Gradually discontinue old system

## Keys to Success

### Technical Aspects

1. **Phased Migration**: Don't change everything at once
2. **Sufficient Testing**: Thoroughly test each phase
3. **Continuous Integration**: Automated testing and building
4. **Performance Monitoring**: Measure performance before and after migration
5. **Apply Modern Java**: try-with-resources, `java.time`, Streams, pattern matching, records

### Organizational Aspects

1. **Management Commitment**: Secure resources and schedule
2. **Team Skill Development**: Continuous learning and training
3. **Clear Communication**: Transparency of progress
4. **Appropriate Risk Management**: Early detection and response to problems

## Expected Benefits

### Short-term Benefits (Within 6 months)

- Significant reduction in security risks
- Improved development productivity (auto-configuration, hot reload, etc.)
- Improved maintainability

### Medium to Long-term Benefits (After 6 months)

- Improved code quality by leveraging latest Java 21 features
- Path to microservices
- Possibility of migrating to cloud-native architecture
- Easier onboarding of new developers
- Rich community support

## Summary

This project uses Apache Struts 1.3.10, a 2008 framework, and has extremely high security risks. This migration plan proposes complete migration to **Java 21 + Spring Boot 3.2.x + Thymeleaf + Spring Data JPA**.

### Migration Benefits

1. **Security**: From EOL Struts 1.x to continuously supported Spring Boot
2. **Productivity**: Auto-configuration, development tools, rich ecosystem
3. **Maintainability**: Modern architecture, clear separation of responsibilities
4. **Future-proofing**: Migration path to microservices and cloud-native
5. **Human Resources**: Abundance of Spring developers, rich learning resources

### Recommended Implementation Approach

**Period**: About 6 months (17 weeks development + testing/deployment/cleanup)

**Resources**: 2-4 developers

**Release Strategy**: Phased migration using Strangler Pattern (recommended)

**Final Phase**: Apply modern Java in Phase 5, completely remove legacy code in Phase 10

### Return on Investment

| Item | Short-term (6 months) | Medium-term (1-2 years) | Long-term (2+ years) |
| --- | --- | --- | --- |
| Development cost | High (migration work) | Low (improved productivity) | Low (easy maintenance) |
| Security risk | Significant reduction | Minimization | Minimization |
| Development speed | Temporary decrease | Improvement | Significant improvement |
| System quality | Improvement | Significant improvement | Significant improvement |

### Final Recommendation

This Apache Struts 1.x application **should start migration immediately**. Considering security risks and technical debt, **complete migration to Spring Boot 3.2.x is the optimal choice**.

- ✅ **Java 21**: LTS support until 2031
- ✅ **Spring Boot 3.2.x**: Industry standard, rich ecosystem
- ✅ **Thymeleaf**: Modern, maintainable template engine
- ✅ **Spring Data JPA**: Declarative, highly productive data access

**3 Actions to Start Immediately:**

1. Explain to and obtain approval from stakeholders
2. Execute POC (Proof of Concept) with Spring Boot
3. Organize team and start training
