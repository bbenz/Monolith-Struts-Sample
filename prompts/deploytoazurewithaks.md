# Deploy Duke's Ski Chalet to AKS

## Goal

Deploy the Spring Boot app in `appmod-migrated-spring-boot-2nd-challenge/` to a **new** Azure Kubernetes Service (AKS) cluster, using a **new** Azure Container Registry (ACR) and Azure Database for PostgreSQL. The existing `Dockerfile` (used for local Docker Compose) is left untouched; a new `Dockerfile.aks` is created for the AKS deployment.

---

## What Gets Deployed

The Spring Boot web app only. The ski-trip-advisor functionality is already built into the web app as `SkiAdvisorController` + `SkiOpenAIService` + `SkiWeatherService`, so there is **no separate service** to deploy.

---

## JAZ (Java Accelerator for Azure)

Use Microsoft's [Azure Command Launcher for Java (`jaz`)](https://learn.microsoft.com/en-us/java/jaz/overview?tabs=ubuntu) in the AKS Dockerfile:

- **Base image:** `mcr.microsoft.com/openjdk/jdk:25-ubuntu` (includes `jaz` pre-installed)
- **Entry point:** `jaz -jar app.jar` instead of `java -jar app.jar`
- **Why:** JAZ auto-detects the container environment and applies battle-tested JVM tuning (heap sizing, GC selection, container-aware memory) — no manual `JAVA_TOOL_OPTIONS` needed
- **Requirement:** JAZ needs a full JDK, not a JRE — hence `openjdk/jdk` not `openjdk/jre`

---

## Files to Create

| File | Purpose |
|------|---------|
| `appmod-migrated-spring-boot-2nd-challenge/Dockerfile.aks` | AKS-optimized Dockerfile using Microsoft OpenJDK + JAZ |
| `appmod-migrated-spring-boot-2nd-challenge/k8s/namespace.yaml` | Kubernetes namespace `dukes-ski-chalet` |
| `appmod-migrated-spring-boot-2nd-challenge/k8s/configmap.yaml` | Non-sensitive config (DB host, ports, weather mode, OpenAI deployment name) |
| `appmod-migrated-spring-boot-2nd-challenge/k8s/secret.yaml` | Sensitive config template (DB creds, OpenAI key) — populated at deploy time |
| `appmod-migrated-spring-boot-2nd-challenge/k8s/deployment.yaml` | App deployment: 2 replicas, resource limits, health probes |
| `appmod-migrated-spring-boot-2nd-challenge/k8s/service.yaml` | LoadBalancer service (port 80 → 8080) |
| `appmod-migrated-spring-boot-2nd-challenge/deploy-to-aks.sh` | End-to-end deploy script |

---

## Azure Resources to Create

All resources will be created in a **new resource group**.

| Resource | Name (suggested) | Details |
|----------|-------------------|---------|
| **Resource Group** | `rg-dukes-ski-chalet` | Location: `eastus2` (or user preference) |
| **Azure Container Registry** | `acrdukesskichalet` | Basic SKU (sufficient for dev/demo) |
| **AKS Cluster** | `aks-dukes-ski-chalet` | 1 node pool, Standard_D2s_v3, Kubernetes ~1.33 |
| **Azure Database for PostgreSQL** | `psql-dukes-ski-chalet` | Flexible Server, Burstable B1ms, database `skishop` |

The deploy script will:
1. Create the resource group (if not exists)
2. Create the ACR
3. Create the PostgreSQL Flexible Server + `skishop` database
4. Load the schema (`01-schema.sql`) and seed data (`02-data.sql`) into PostgreSQL
5. Create the AKS cluster with ACR attached
6. Build and push the Docker image (using `Dockerfile.aks`)
7. Apply Kubernetes manifests (substituting real values into ConfigMap/Secret templates)
8. Wait for rollout and report the external IP

---

## Dockerfile.aks

```dockerfile
FROM maven:4.0.0-rc-5-eclipse-temurin-25 AS build
WORKDIR /workspace
COPY pom.xml spring-boot-app/pom.xml
RUN mvn -f pom.xml -B dependency:go-offline || true
COPY /src spring-boot-app/src
RUN mvn -f spring-boot-app/pom.xml -B -DskipTests package

# Runtime — Microsoft OpenJDK with JAZ pre-installed
FROM mcr.microsoft.com/openjdk/jdk:25-ubuntu AS runtime
WORKDIR /app
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*
COPY --from=build /workspace/spring-boot-app/target/*.jar /app/app.jar
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s --retries=5 CMD curl -f http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["jaz", "-jar", "/app/app.jar"]
```

Key differences from the local `Dockerfile`:
- Runtime base: `mcr.microsoft.com/openjdk/jdk:25-ubuntu` (full JDK with JAZ) instead of `eclipse-temurin:25-jre`
- Entry point: `jaz` instead of `java`
- No `JAVA_TOOL_OPTIONS` — JAZ handles JVM tuning automatically

---

## Environment Variables

The app reads these from `application.yml` (all have env-var overrides):

| Variable | Source | Example |
|----------|--------|---------|
| `DB_HOST` | ConfigMap | `psql-dukes-ski-chalet.postgres.database.azure.com` |
| `DB_PORT` | ConfigMap | `5432` |
| `DB_NAME` | ConfigMap | `skishop` |
| `DB_USER` | Secret | `skishop` |
| `DB_PASSWORD` | Secret | *(generated at deploy time)* |
| `OPENAI_ENDPOINT` | Secret | *(from existing Azure OpenAI)* |
| `OPENAI_APIKEY` | Secret | *(from existing Azure OpenAI)* |
| `OPENAI_DEPLOYMENT` | ConfigMap | `gpt-5.2-chat` |
| `WEATHER_MODE` | ConfigMap | `real` |
| `WEATHER_FALLBACK` | ConfigMap | `true` |

---

## Database Initialization

The PostgreSQL Flexible Server database will be initialized using the existing SQL scripts:
- `src/main/resources/db/01-schema.sql` — table definitions
- `src/main/resources/db/02-data.sql` — seed data (categories, products, prices)

These will be applied via `psql` after the server is created, reusing the same scripts that Docker Compose mounts into `/docker-entrypoint-initdb.d`.

---

## Security Notes

- The `secret.yaml` is a **template** with placeholders — actual values are injected by the deploy script via `sed` and never committed to source control
- For production, replace Kubernetes Secrets with **Azure Key Vault + CSI Secrets Store Driver**
- PostgreSQL uses SSL enforcement by default on Azure Flexible Server
- ACR is attached to AKS via managed identity (no image pull secrets needed)

---

## What Is NOT Changed

- The existing `Dockerfile` — continues to work for `docker compose up` locally
- The existing `docker-compose.yml` — continues to work for local dev
- The Spring Boot application code — zero code changes required
- The `application.yml` — already parameterized with `${ENV_VAR:default}` pattern

---

## Prerequisites

- Azure CLI (`az`) logged in to the target subscription
- Docker running (for image build)
- `kubectl` installed (usually comes with `az aks install-cli`)
- `psql` client installed (for database initialization)

---

## Usage

After all files are created, deploy with:

```bash
cd appmod-migrated-spring-boot-2nd-challenge
./deploy-to-aks.sh \
  --location eastus2 \
  --openai-endpoint <your-azure-openai-endpoint> \
  --openai-apikey <your-api-key>
```

The script handles resource group, ACR, PostgreSQL, AKS creation, image build/push, and K8s deployment automatically.
