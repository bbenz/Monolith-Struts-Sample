# Multi-stage build for Spring Boot 3.2.12 + Java 25
# Stage 1: Build with Maven
FROM maven:4.0.0-rc-5-eclipse-temurin-25-alpine AS build

WORKDIR /workspace

# Copy Maven configuration and dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline -B || true

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Runtime with Java 25
FROM eclipse-temurin:25-jre-alpine AS runtime

# Install required packages
RUN apk add --no-cache curl bash

# Create application user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

# Copy JAR from build stage
COPY --from=build /workspace/target/*.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run Spring Boot application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
