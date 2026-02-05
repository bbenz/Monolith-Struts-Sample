# SkiShop - Legacy to Modern Application Migration Demo

This repository demonstrates the complete journey of modernizing a legacy monolithic web application, from its original Java 5 + Struts 1.x implementation through migration to Java 21 + Spring Boot, showcasing the power of AI-assisted development and migration tools.

## 📖 Project Overview

This project recreates a monolithic e-commerce web application from approximately 20 years ago to demonstrate modern application modernization techniques. The codebase includes three distinct versions of the same SkiShop application, each representing a different stage in the modernization journey:

1. **Original Legacy Application** - Java 5 + Struts 1.2.9 (circa 2004)
2. **First Migration** - Java 21 + Spring Boot 3.2 with Thymeleaf
3. **Enhanced Migration** - Java 21 + Spring Boot 3.2 with modern architecture patterns

### Development Process

The development of this application followed a systematic, AI-assisted approach:

**Planning Phase**
   - Created a [detailed design document](monolith-struts.md) specifying the legacy application architecture
   - Developed an [implementation plan](impl-plan.md) outlining the development phases
   - Defined clear requirements and validation criteria for each phase

**Migration to Modern Stack**
   - Used GitHub Copilot App Modernization Agent to migrate to Java 21 and Spring Boot
   - Generated [UPGRADE_PLAN.md](UPGRADE_PLAN.md) with detailed migration strategy
   - Enhanced the plan to include:
     - Latest Spring Boot best practices
     - Migration to Spring Data JPA and Thymeleaf
     - Removal of legacy Struts code and project cleanup
   - Executed automated migration with post-migration refinements

**Key Success Factors:**
- Incorporated validation phases at each step to ensure quality
- Thoroughly reviewed all generated code before proceeding
- Maintained clear documentation throughout the process
- Leveraged AI tools for both initial development and modernization

---

## 🚀 Running the Applications

This repository contains three versions of the SkiShop application. Each can be run independently.

### Version 1: Original Legacy Application (Java 5 + Struts 1.2.9)

Located in the root `src/` directory.

**Requirements:**
- Java 8 runtime (or use the provided Docker setup with Tomcat 6.0.53 + JDK 5.0u22)
- Maven 3.x (for build/test)
- PostgreSQL 9.2+ (schema in `src/main/resources/db/schema.sql`)

**Build & Run:**
```sh
# Build the WAR file
mvn -B test
mvn -B package

# The WAR is generated at target/skishop-monolith.war

# Run with Docker (recommended)
docker-compose up --build
```

**Access the application:**
- URL: http://localhost:8080/
- The Docker setup includes both the Tomcat 6 server and PostgreSQL database

**Configuration:**
- `app.properties` is in `src/main/resources/` and supports `${TOKEN}` placeholders
- Environment variables can be set for database credentials (e.g., `DB_PASSWORD`)
- JNDI configuration example: `src/main/webapp/META-INF/context.xml`

**Note:** Docker builds download JDK 5.0u22; pass `--build-arg JDK_LICENSE=accept` to acknowledge license terms.

---

### Version 2: First Spring Boot Migration (Java 21 + Spring Boot 3.2)

Located in the `appmod-migrated-java21-spring-boot/` directory.

**Requirements:**
- Java 21
- Docker & Docker Compose (recommended)

**Build & Run:**
```sh
cd appmod-migrated-java21-spring-boot

# Run with Docker Compose (PostgreSQL included)
docker-compose up -d --build

# Or build and run locally
mvn clean package
java -jar target/skishop-0.0.1-SNAPSHOT.jar
```

**Access the application:**
- URL: http://localhost:8080/
- Database: PostgreSQL (auto-configured via Docker Compose)

**Features:**
- Migrated from Struts 1.x to Spring Boot 3.2
- JSP templates converted to Thymeleaf
- Modern dependency injection with Spring
- RESTful controller architecture
- Enhanced error handling and validation

**Documentation:**
- [Migration Report](appmod-migrated-java21-spring-boot/MIGRATION_REPORT.md)
- [JSP to Thymeleaf Migration](appmod-migrated-java21-spring-boot/JSP_TO_THYMELEAF_MIGRATION.md)
- [Docker Guide](appmod-migrated-java21-spring-boot/DOCKER_GUIDE.md)

---

### Version 3: Enhanced Migration (Java 21 + Spring Boot 3.2)

Located in the `appmod-migrated-java21-spring-boot-2nd-challenge/` directory.

**Requirements:**
- Java 21
- Docker & Docker Compose

**Build & Run:**
```sh
cd appmod-migrated-java21-spring-boot-2nd-challenge

# Run with Docker Compose
docker-compose up -d --build
```

**Access the application:**
- URL: http://localhost:8080/
- Admin Demo Login: `admin@example.com` (see docs for credentials)

**Enhanced Features:**
- Additional admin user for demo purposes
- Improved error handling with custom error pages
- Enhanced UI/UX with modern design patterns
- Optimized database queries and caching
- Production-ready configuration

**Documentation:**
- [Quick Start Guide](appmod-migrated-java21-spring-boot-2nd-challenge/README.md)
- [Architecture Overview](appmod-migrated-java21-spring-boot-2nd-challenge/docs/architecture.md)
- [Operations Guide](appmod-migrated-java21-spring-boot-2nd-challenge/docs/operations.md)

---

## ☁️ Deploying to Azure

The enhanced migration (Version 3) is designed for cloud deployment and can be easily deployed to Azure.

### Prerequisites

- Azure CLI installed and configured
- Azure subscription with appropriate permissions
- Docker installed locally (for container image building)

### Deployment Options

#### Option 1: Deploy to Azure App Service

```sh
cd appmod-migrated-java21-spring-boot-2nd-challenge

# Login to Azure
az login

# Create resource group
az group create --name skishop-rg --location eastus

# Create Azure Database for PostgreSQL
az postgres flexible-server create \
  --resource-group skishop-rg \
  --name skishop-db \
  --location eastus \
  --admin-user skishopadmin \
  --admin-password <your-secure-password> \
  --sku-name Standard_B1ms \
  --version 14

# Create the database
az postgres flexible-server db create \
  --resource-group skishop-rg \
  --server-name skishop-db \
  --database-name skishop

# Build and deploy to App Service
mvn clean package -DskipTests
az webapp up \
  --resource-group skishop-rg \
  --name skishop-app \
  --runtime JAVA:21-java21 \
  --sku B1

# Configure environment variables
az webapp config appsettings set \
  --resource-group skishop-rg \
  --name skishop-app \
  --settings \
    SPRING_DATASOURCE_URL="jdbc:postgresql://skishop-db.postgres.database.azure.com:5432/skishop" \
    SPRING_DATASOURCE_USERNAME="skishopadmin" \
    SPRING_DATASOURCE_PASSWORD="<your-secure-password>"
```

#### Option 2: Deploy to Azure Kubernetes Service (AKS)

**Step 1: Set up AKS Cluster**

```sh
# Create AKS cluster
az aks create \
  --resource-group skishop-rg \
  --name skishop-aks \
  --node-count 2 \
  --node-vm-size Standard_B2s \
  --enable-addons monitoring \
  --generate-ssh-keys

# Get credentials
az aks get-credentials \
  --resource-group skishop-rg \
  --name skishop-aks
```

**Step 2: Set up Azure Container Registry (ACR)**

```sh
# Create ACR
az acr create \
  --resource-group skishop-rg \
  --name skishopacr \
  --sku Basic

# Attach ACR to AKS
az aks update \
  --resource-group skishop-rg \
  --name skishop-aks \
  --attach-acr skishopacr

# Build and push Docker image
az acr build \
  --registry skishopacr \
  --image skishop:latest \
  --file Dockerfile .
```

**Step 3: Deploy PostgreSQL on Azure**

```sh
# Create PostgreSQL (same as above)
az postgres flexible-server create \
  --resource-group skishop-rg \
  --name skishop-db \
  --location eastus \
  --admin-user skishopadmin \
  --admin-password <your-secure-password> \
  --sku-name Standard_B1ms \
  --version 14

# Allow Azure services access
az postgres flexible-server firewall-rule create \
  --resource-group skishop-rg \
  --name skishop-db \
  --rule-name AllowAzureServices \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 0.0.0.0
```

**Step 4: Create Kubernetes Deployment Files**

Create `k8s/deployment.yaml`:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: skishop
spec:
  replicas: 2
  selector:
    matchLabels:
      app: skishop
  template:
    metadata:
      labels:
        app: skishop
    spec:
      containers:
      - name: skishop
        image: skishopacr.azurecr.io/skishop:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_DATASOURCE_URL
          value: "jdbc:postgresql://skishop-db.postgres.database.azure.com:5432/skishop"
        - name: SPRING_DATASOURCE_USERNAME
          value: "skishopadmin"
        - name: SPRING_DATASOURCE_PASSWORD
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: password
---
apiVersion: v1
kind: Service
metadata:
  name: skishop
spec:
  type: LoadBalancer
  ports:
  - port: 80
    targetPort: 8080
  selector:
    app: skishop
```

**Step 5: Create Secrets and Deploy**

```sh
# Create database password secret
kubectl create secret generic db-secret \
  --from-literal=password='<your-secure-password>'

# Apply deployment
kubectl apply -f k8s/deployment.yaml

# Get external IP
kubectl get service skishop
```

**Step 6: Configure Ingress (Optional)**

```sh
# Install NGINX Ingress Controller
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.8.1/deploy/static/provider/cloud/deploy.yaml

# Create ingress resource
kubectl apply -f k8s/ingress.yaml
```

### Post-Deployment Steps

1. **Initialize the database:**
   ```sh
   # Connect to the PostgreSQL database and run:
   # - src/main/resources/db/schema.sql
   # - src/main/resources/db/data.sql (or data-EN.sql for English version)
   ```

2. **Verify the deployment:**
   - Access the application URL
   - Test login functionality
   - Verify database connectivity

3. **Monitor the application:**
   ```sh
   # For App Service
   az webapp log tail --resource-group skishop-rg --name skishop-app
   
   # For AKS
   kubectl logs -f deployment/skishop
   ```

### Additional Resources

- [Azure App Service Documentation](https://docs.microsoft.com/azure/app-service/)
- [Azure Kubernetes Service Documentation](https://docs.microsoft.com/azure/aks/)
- [Azure Database for PostgreSQL Documentation](https://docs.microsoft.com/azure/postgresql/)

---

## 📚 Documentation

### Core Documentation
- [Detailed Design Document](monolith-struts.md) - Original Struts 1.x application design
- [Implementation Plan](impl-plan.md) - Phase-by-phase development plan
- [Upgrade Plan](UPGRADE_PLAN.md) - Migration strategy to Java 21 + Spring Boot

### Migration Reports
- [Migration Completion Report](appmod-migrated-java21-spring-boot/MIGRATION_REPORT.md)
- [JSP to Thymeleaf Migration](appmod-migrated-java21-spring-boot/JSP_TO_THYMELEAF_MIGRATION.md)
- [Project Cleanup Report](appmod-migrated-java21-spring-boot/PROJECT_CLEANUP_REPORT.md)

### English Translations
All documentation is available in English with `-EN` suffix:
- [Detailed Design (EN)](monolith-struts-EN.md)
- [Implementation Plan (EN)](impl-plan-EN.md)
- [Upgrade Plan (EN)](UPGRADE_PLAN-EN.md)

### Operations & Deployment
- [Operations Guide](docs/ops.md) - Tomcat 6 deployment for legacy version
- [Docker Guide](appmod-migrated-java21-spring-boot/DOCKER_GUIDE.md) - Container deployment
- [Architecture Guide](appmod-migrated-java21-spring-boot-2nd-challenge/docs/architecture.md)

---

## 🛠️ Technology Stack

### Legacy Version (Version 1)
- Java 5
- Struts 1.2.9
- JSP/JSTL
- Apache Tiles
- PostgreSQL 9.2
- Tomcat 6.0.53
- Maven

### Modern Versions (Versions 2 & 3)
- Java 21
- Spring Boot 3.2
- Spring Web MVC
- Spring Data JPA
- Thymeleaf
- PostgreSQL 14+
- Docker & Docker Compose
- Maven

---

## 📝 License

This project is created for demonstration and educational purposes to showcase application modernization techniques using AI-assisted development tools.

---

## 🤝 Contributing

This is a demonstration project showcasing the migration journey. Feel free to use it as a reference for your own modernization projects.

---

## 📧 Contact

For questions about the migration process or AI-assisted development techniques demonstrated in this project, please open an issue in this repository.
