FROM maven:4.0.0-rc-5-eclipse-temurin-25 AS build

WORKDIR /workspace

# Preload dependencies for Spring Boot app
# COPY .mvn .mvn
COPY pom.xml spring-boot-app/pom.xml
RUN mvn -f pom.xml -B dependency:go-offline || true

# Build application (jar)
COPY /src spring-boot-app/src
RUN mvn -f spring-boot-app/pom.xml -B -DskipTests package

# Runtime
FROM eclipse-temurin:25-jre AS runtime
WORKDIR /app

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:+UseG1GC"

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

COPY --from=build /workspace/spring-boot-app/target/*.jar /app/app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --retries=5 CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
