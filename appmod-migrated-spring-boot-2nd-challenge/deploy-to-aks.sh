#!/usr/bin/env bash
set -euo pipefail

###############################################################################
# deploy-to-aks.sh — End-to-end deployment of Duke's Ski Chalet to AKS
#
# Usage:
#   ./deploy-to-aks.sh \
#     --location eastus2 \
#     --openai-endpoint <your-azure-openai-endpoint> \
#     --openai-apikey <your-api-key>
#
# Optional flags:
#   --resource-group <name>   (default: rg-dukes-ski-chalet)
#   --acr-name <name>         (default: acrdukesskichalet)
#   --aks-name <name>         (default: aks-dukes-ski-chalet)
#   --psql-name <name>        (default: psql-dukes-ski-chalet)
#   --db-name <name>          (default: skishop)
#   --db-user <name>          (default: skishop)
#   --db-password <pw>        (auto-generated if not set)
#   --openai-deployment <name>(default: gpt-5.2-chat)
###############################################################################

# ── Defaults ─────────────────────────────────────────────────────────────────
LOCATION=""
RESOURCE_GROUP="rg-dukes-ski-chalet"
ACR_NAME="acrdukesskichalet"
AKS_NAME="aks-dukes-ski-chalet"
PSQL_NAME="psql-dukes-ski-chalet"
DB_NAME="skishop"
DB_USER="skishop"
DB_PASSWORD=""
OPENAI_ENDPOINT=""
OPENAI_APIKEY=""
OPENAI_DEPLOYMENT="gpt-5.2-chat"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
K8S_DIR="${SCRIPT_DIR}/k8s"
DB_DIR="${SCRIPT_DIR}/src/main/resources/db"

# ── Parse arguments ──────────────────────────────────────────────────────────
while [[ $# -gt 0 ]]; do
  case "$1" in
    --location)           LOCATION="$2";           shift 2 ;;
    --resource-group)     RESOURCE_GROUP="$2";     shift 2 ;;
    --acr-name)           ACR_NAME="$2";           shift 2 ;;
    --aks-name)           AKS_NAME="$2";           shift 2 ;;
    --psql-name)          PSQL_NAME="$2";          shift 2 ;;
    --db-name)            DB_NAME="$2";            shift 2 ;;
    --db-user)            DB_USER="$2";            shift 2 ;;
    --db-password)        DB_PASSWORD="$2";        shift 2 ;;
    --openai-endpoint)    OPENAI_ENDPOINT="$2";    shift 2 ;;
    --openai-apikey)      OPENAI_APIKEY="$2";      shift 2 ;;
    --openai-deployment)  OPENAI_DEPLOYMENT="$2";  shift 2 ;;
    *) echo "Unknown option: $1"; exit 1 ;;
  esac
done

# ── Validate required arguments ──────────────────────────────────────────────
if [[ -z "$LOCATION" ]]; then
  echo "ERROR: --location is required (e.g., eastus2)"
  exit 1
fi
if [[ -z "$OPENAI_ENDPOINT" ]]; then
  echo "ERROR: --openai-endpoint is required"
  exit 1
fi
if [[ -z "$OPENAI_APIKEY" ]]; then
  echo "ERROR: --openai-apikey is required"
  exit 1
fi

# Auto-generate a DB password if not provided
if [[ -z "$DB_PASSWORD" ]]; then
  DB_PASSWORD="$(openssl rand -base64 24 | tr -d '/+=' | head -c 24)Aa1!"
  echo "Generated DB password (save this): ${DB_PASSWORD}"
fi

IMAGE_TAG="${ACR_NAME}.azurecr.io/skishop-app:latest"

echo "============================================================"
echo " Duke's Ski Chalet — AKS Deployment"
echo "============================================================"
echo " Location:         ${LOCATION}"
echo " Resource Group:   ${RESOURCE_GROUP}"
echo " ACR:              ${ACR_NAME}"
echo " AKS:              ${AKS_NAME}"
echo " PostgreSQL:       ${PSQL_NAME}"
echo " Database:         ${DB_NAME}"
echo " Image:            ${IMAGE_TAG}"
echo "============================================================"

# ── 1. Resource Group ────────────────────────────────────────────────────────
echo ""
echo "▶ Step 1/8: Creating resource group..."
az group create --name "$RESOURCE_GROUP" --location "$LOCATION" --output none
echo "  ✓ Resource group '${RESOURCE_GROUP}' ready"

# ── 2. Azure Container Registry ─────────────────────────────────────────────
echo ""
echo "▶ Step 2/8: Creating Azure Container Registry..."
az acr create \
  --resource-group "$RESOURCE_GROUP" \
  --name "$ACR_NAME" \
  --sku Basic \
  --output none
echo "  ✓ ACR '${ACR_NAME}' ready"

# ── 3. PostgreSQL Flexible Server ────────────────────────────────────────────
echo ""
echo "▶ Step 3/8: Creating Azure Database for PostgreSQL..."
az postgres flexible-server create \
  --resource-group "$RESOURCE_GROUP" \
  --name "$PSQL_NAME" \
  --location "$LOCATION" \
  --admin-user "$DB_USER" \
  --admin-password "$DB_PASSWORD" \
  --sku-name Standard_B1ms \
  --tier Burstable \
  --storage-size 32 \
  --version 15 \
  --public-access 0.0.0.0 \
  --yes \
  --output none
echo "  ✓ PostgreSQL server '${PSQL_NAME}' ready"

# Create the database
echo "  Creating database '${DB_NAME}'..."
az postgres flexible-server db create \
  --resource-group "$RESOURCE_GROUP" \
  --server-name "$PSQL_NAME" \
  --database-name "$DB_NAME" \
  --output none
echo "  ✓ Database '${DB_NAME}' created"

# ── 4. Initialize Database ──────────────────────────────────────────────────
echo ""
echo "▶ Step 4/8: Initializing database with schema and seed data..."
DB_HOST="${PSQL_NAME}.postgres.database.azure.com"

export PGPASSWORD="$DB_PASSWORD"
psql -h "$DB_HOST" -U "$DB_USER" -d "$DB_NAME" --set=sslmode=require \
  -f "${DB_DIR}/01-schema.sql"
echo "  ✓ Schema loaded"

psql -h "$DB_HOST" -U "$DB_USER" -d "$DB_NAME" --set=sslmode=require \
  -f "${DB_DIR}/02-data.sql"
echo "  ✓ Seed data loaded"
unset PGPASSWORD

# ── 5. AKS Cluster ──────────────────────────────────────────────────────────
echo ""
echo "▶ Step 5/8: Creating AKS cluster (this may take several minutes)..."
az aks create \
  --resource-group "$RESOURCE_GROUP" \
  --name "$AKS_NAME" \
  --location "$LOCATION" \
  --node-count 1 \
  --node-vm-size Standard_D2s_v3 \
  --attach-acr "$ACR_NAME" \
  --generate-ssh-keys \
  --enable-managed-identity \
  --output none
echo "  ✓ AKS cluster '${AKS_NAME}' ready"

# Get credentials
echo "  Fetching kubeconfig..."
az aks get-credentials \
  --resource-group "$RESOURCE_GROUP" \
  --name "$AKS_NAME" \
  --overwrite-existing
echo "  ✓ kubeconfig updated"

# ── 6. Build and Push Docker Image ──────────────────────────────────────────
echo ""
echo "▶ Step 6/8: Building and pushing Docker image..."
az acr login --name "$ACR_NAME"

docker build \
  -t "$IMAGE_TAG" \
  -f "${SCRIPT_DIR}/Dockerfile.aks" \
  "$SCRIPT_DIR"

docker push "$IMAGE_TAG"
echo "  ✓ Image pushed to ${IMAGE_TAG}"

# ── 7. Apply Kubernetes Manifests ────────────────────────────────────────────
echo ""
echo "▶ Step 7/8: Applying Kubernetes manifests..."

# Create namespace
kubectl apply -f "${K8S_DIR}/namespace.yaml"

# Apply ConfigMap with real values
sed \
  -e "s|PLACEHOLDER_DB_HOST|${DB_HOST}|g" \
  "${K8S_DIR}/configmap.yaml" | kubectl apply -f -

# Apply Secret with real values
sed \
  -e "s|PLACEHOLDER_DB_USER|${DB_USER}|g" \
  -e "s|PLACEHOLDER_DB_PASSWORD|${DB_PASSWORD}|g" \
  -e "s|PLACEHOLDER_OPENAI_ENDPOINT|${OPENAI_ENDPOINT}|g" \
  -e "s|PLACEHOLDER_OPENAI_APIKEY|${OPENAI_APIKEY}|g" \
  "${K8S_DIR}/secret.yaml" | kubectl apply -f -

# Apply Deployment with real image
sed \
  -e "s|PLACEHOLDER_IMAGE|${IMAGE_TAG}|g" \
  "${K8S_DIR}/deployment.yaml" | kubectl apply -f -

# Apply Service
kubectl apply -f "${K8S_DIR}/service.yaml"

echo "  ✓ All manifests applied"

# ── 8. Wait for Rollout ─────────────────────────────────────────────────────
echo ""
echo "▶ Step 8/8: Waiting for rollout to complete..."
kubectl rollout status deployment/skishop-app \
  --namespace dukes-ski-chalet \
  --timeout=300s

echo ""
echo "  Waiting for external IP..."
EXTERNAL_IP=""
for i in $(seq 1 60); do
  EXTERNAL_IP=$(kubectl get svc skishop-service \
    --namespace dukes-ski-chalet \
    -o jsonpath='{.status.loadBalancer.ingress[0].ip}' 2>/dev/null || true)
  if [[ -n "$EXTERNAL_IP" ]]; then
    break
  fi
  sleep 5
done

echo ""
echo "============================================================"
echo " Deployment Complete!"
echo "============================================================"
if [[ -n "$EXTERNAL_IP" ]]; then
  echo " App URL:      http://${EXTERNAL_IP}"
else
  echo " External IP not yet assigned. Check with:"
  echo "   kubectl get svc skishop-service -n dukes-ski-chalet"
fi
echo ""
echo " PostgreSQL:   ${DB_HOST}"
echo " Database:     ${DB_NAME}"
echo " DB User:      ${DB_USER}"
echo "============================================================"
