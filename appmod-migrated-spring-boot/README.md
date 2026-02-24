# SkiShop - Spring Boot Application

Modern e-commerce application built with Spring Boot, migrated from legacy Struts 1.3 monolith.

## Tech Stack
- **Java**: 25 LTS
- **Framework**: Spring Boot 3.5.0
- **Template Engine**: Thymeleaf 3.x
- **Database**: PostgreSQL 15
- **Build Tool**: Maven 3.9.x
- **Container**: Docker + Docker Compose

## Architecture
- **Backend**: Spring MVC with RESTful controllers
- **Frontend**: Thymeleaf templates with modern CSS
- **Data Access**: Spring Data JDBC
- **Security**: Spring Security (CSRF protection)
- **Validation**: Jakarta Bean Validation

## Prerequisites
- Docker & Docker Compose
- Java 25 (for local development)
- Maven 3.9+ (for local development)

## Quick Start with Docker

### 1. Build and Run
```bash
docker-compose up -d --build
```

### 2. Access the Application
- **URL**: http://localhost:8080
- **Health Check**: http://localhost:8080/actuator/health

### 3. Stop the Application
```bash
docker-compose down
```

## Local Development

### Build
```bash
mvn clean package
```

### Run with Dev Profile
```bash
SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run
```

### Run Tests
```bash
mvn test
```

## Database Schema
Database schema and initial data are automatically initialized from:
- `src/main/resources/db/schema.sql` - Table definitions
- `src/main/resources/db/data.sql` - Sample data

## Configuration

### Application Profiles
- **dev**: Development profile (PostgreSQL in Docker)
- **prod**: Production profile (external PostgreSQL)

### Environment Variables
```bash
# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/skishop
DB_USERNAME=skishop_user
DB_PASSWORD=skishop_pass

# Application Settings
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=dev
```

## Project Structure
```
src/
├── main/
│   ├── java/com/skishop/
│   │   ├── domain/          # Domain models
│   │   ├── dao/             # Data access layer
│   │   ├── service/         # Business logic
│   │   ├── web/
│   │   │   ├── controller/  # Spring MVC controllers
│   │   │   └── dto/         # Data transfer objects
│   │   └── SkiShopApplication.java
│   └── resources/
│       ├── templates/       # Thymeleaf templates
│       │   ├── fragments/   # Reusable fragments
│       │   ├── products/
│       │   ├── cart/
│       │   ├── orders/
│       │   └── ...
│       ├── static/
│       │   └── css/         # Stylesheets
│       ├── db/              # Database scripts
│       └── application.properties
└── test/                    # Test code
```

## Key Features
- Product catalog with search and filtering
- Shopping cart management
- Order processing and history
- User authentication and authorization
- Coupon and discount system
- Points/rewards program
- Address management
- Admin panel for products, orders, and coupons

## API Endpoints

### Public
- `GET /` - Home page
- `GET /products` - Product listing
- `GET /login` - Login page
- `GET /register` - Registration page

### Authenticated
- `GET /cart` - Shopping cart
- `GET /checkout` - Checkout page
- `GET /orders` - Order history
- `GET /points/balance` - Points balance
- `GET /addresses` - Address management

### Admin
- `GET /admin/products` - Product management
- `GET /admin/orders` - Order management
- `GET /admin/coupons` - Coupon management

## Docker Configuration

### Services
1. **PostgreSQL**: Database server (port 5432)
2. **Spring Boot App**: Application server (port 8080)

### Volumes
- `postgres_data`: Persistent database storage

## Migration History
This application was successfully migrated from:
- **Java 1.5** → **Java 25**
- **Struts 1.3** → **Spring Boot 3.2**
- **JSP** → **Thymeleaf**
- **Tomcat 6** → **Embedded Tomcat**

See migration documentation:
- [MIGRATION_REPORT.md](MIGRATION_REPORT.md)
- [THYMELEAF_MIGRATION_COMPLETE.md](THYMELEAF_MIGRATION_COMPLETE.md)
- [CONTROLLER_TEMPLATE_FIX.md](CONTROLLER_TEMPLATE_FIX.md)

## License
Proprietary - SkiShop Application

## Support
For issues or questions, please contact the development team.
