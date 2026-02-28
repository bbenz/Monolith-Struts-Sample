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
LOCATION="swedencentral"
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

# ── Helper: extract default value from application.yml ───────────────────────
# Parses lines like:  key: ${ENV_VAR:default_value}
# Returns the default_value portion, or empty string if not found.
APP_YML="${SCRIPT_DIR}/src/main/resources/application.yml"

extract_yml_default() {
  local env_var_name="$1"
  if [[ -f "$APP_YML" ]]; then
    # Match ${ENV_VAR:default} and extract the default after the colon
    grep -oP "\\\$\{${env_var_name}:\K[^}]+" "$APP_YML" 2>/dev/null | head -1 || true
  fi
}

# ── Resolve variables: CLI arg → exported env var → application.yml default ──
resolve_var() {
  local current_val="$1"
  local env_var_name="$2"
  local source=""

  # 1. CLI arg already set — keep it
  if [[ -n "$current_val" ]]; then
    return
  fi

  # 2. Exported environment variable
  local env_val="${!env_var_name:-}"
  if [[ -n "$env_val" ]]; then
    echo "$env_val"
    return
  fi

  # 3. Default from application.yml
  local yml_val
  yml_val="$(extract_yml_default "$env_var_name")"
  if [[ -n "$yml_val" ]]; then
    echo "$yml_val"
    return
  fi
}

echo ""
echo "Resolving configuration (CLI args → env vars → application.yml)..."

# Required variables — try fallback chain
if [[ -z "$OPENAI_ENDPOINT" ]]; then
  OPENAI_ENDPOINT="$(resolve_var "$OPENAI_ENDPOINT" "OPENAI_ENDPOINT")"
  [[ -n "$OPENAI_ENDPOINT" ]] && echo "  OPENAI_ENDPOINT resolved from env/application.yml"
fi
if [[ -z "$OPENAI_APIKEY" ]]; then
  OPENAI_APIKEY="$(resolve_var "$OPENAI_APIKEY" "OPENAI_APIKEY")"
  [[ -n "$OPENAI_APIKEY" ]] && echo "  OPENAI_APIKEY resolved from env/application.yml"
fi

# Optional variables — try fallback chain for overrides
if [[ -z "$OPENAI_DEPLOYMENT" || "$OPENAI_DEPLOYMENT" == "gpt-5.2-chat" ]]; then
  resolved="$(resolve_var "" "OPENAI_DEPLOYMENT")"
  if [[ -n "$resolved" ]]; then
    OPENAI_DEPLOYMENT="$resolved"
    echo "  OPENAI_DEPLOYMENT resolved from env/application.yml"
  fi
fi
if [[ -z "$DB_NAME" || "$DB_NAME" == "skishop" ]]; then
  resolved="$(resolve_var "" "DB_NAME")"
  if [[ -n "$resolved" ]]; then
    DB_NAME="$resolved"
    echo "  DB_NAME resolved from env/application.yml"
  fi
fi
if [[ -z "$DB_USER" || "$DB_USER" == "skishop" ]]; then
  resolved="$(resolve_var "" "DB_USER")"
  if [[ -n "$resolved" ]]; then
    DB_USER="$resolved"
    echo "  DB_USER resolved from env/application.yml"
  fi
fi
if [[ -z "$DB_PASSWORD" ]]; then
  resolved="$(resolve_var "" "DB_PASSWORD")"
  if [[ -n "$resolved" && "$resolved" != "password" ]]; then
    DB_PASSWORD="$resolved"
    echo "  DB_PASSWORD resolved from env/application.yml"
  fi
fi

# ── Validate required arguments ──────────────────────────────────────────────
MISSING=()
if [[ -z "$LOCATION" ]]; then
  MISSING+=("LOCATION (use --location or export LOCATION)")
fi
if [[ -z "$OPENAI_ENDPOINT" ]]; then
  MISSING+=("OPENAI_ENDPOINT (use --openai-endpoint, export OPENAI_ENDPOINT, or set in application.yml)")
fi
if [[ -z "$OPENAI_APIKEY" ]]; then
  MISSING+=("OPENAI_APIKEY (use --openai-apikey, export OPENAI_APIKEY, or set in application.yml)")
fi

if [[ ${#MISSING[@]} -gt 0 ]]; then
  echo ""
  echo "ERROR: The following required variables are missing:"
  for var in "${MISSING[@]}"; do
    echo "  ✗ ${var}"
  done
  echo ""
  echo "Provide them via CLI flags, exported environment variables, or"
  echo "defaults in src/main/resources/application.yml"
  exit 1
fi

# Auto-generate a DB password if not provided
DB_PASSWORD_WAS_GENERATED="false"
if [[ -z "$DB_PASSWORD" ]]; then
  DB_PASSWORD="$(openssl rand -base64 24 | tr -d '/+=' | head -c 24)Aa1!"
  DB_PASSWORD_WAS_GENERATED="true"
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
if az group exists --name "$RESOURCE_GROUP" 2>/dev/null | grep -q true; then
  echo "  ⏩ Resource group '${RESOURCE_GROUP}' already exists — reusing"
else
  az group create --name "$RESOURCE_GROUP" --location "$LOCATION" --output none
  echo "  ✓ Resource group '${RESOURCE_GROUP}' created"
fi

# ── 2. Azure Container Registry ─────────────────────────────────────────────
echo ""
echo "▶ Step 2/8: Creating Azure Container Registry..."
if az acr show --name "$ACR_NAME" --resource-group "$RESOURCE_GROUP" --output none 2>/dev/null; then
  echo "  ⏩ ACR '${ACR_NAME}' already exists — reusing"
else
  az acr create \
    --resource-group "$RESOURCE_GROUP" \
    --name "$ACR_NAME" \
    --sku Basic \
    --output none
  echo "  ✓ ACR '${ACR_NAME}' created"
fi

# ── 3. PostgreSQL Flexible Server ────────────────────────────────────────────
echo ""
echo "▶ Step 3/8: Creating Azure Database for PostgreSQL..."
PSQL_EXISTS=false
if az postgres flexible-server show --name "$PSQL_NAME" --resource-group "$RESOURCE_GROUP" --output none 2>/dev/null; then
  PSQL_EXISTS=true
  echo "  ⏩ PostgreSQL server '${PSQL_NAME}' already exists — reusing"
  if [[ "$DB_PASSWORD_WAS_GENERATED" == "true" ]]; then
    echo ""
    echo "  ERROR: Server already exists but no password was provided."
    echo "  On re-runs you must supply the original password via:"
    echo "    --db-password <pw>  or  export DB_PASSWORD=<pw>"
    echo ""
    echo "  If you've lost the password, reset it with:"
    echo "    az postgres flexible-server update \\"
    echo "      --resource-group $RESOURCE_GROUP --name $PSQL_NAME \\"
    echo "      --admin-password <new-password>"
    exit 1
  fi
  # Update the password on the existing server to match what was provided
  echo "  Updating admin password on existing server..."
  az postgres flexible-server update \
    --resource-group "$RESOURCE_GROUP" \
    --name "$PSQL_NAME" \
    --admin-password "$DB_PASSWORD" \
    --output none
  echo "  ✓ Admin password updated"
else
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
  echo "  ✓ PostgreSQL server '${PSQL_NAME}' created"
fi

# Add firewall rule for the current deployer's IP
echo "  Configuring firewall rules..."
DEPLOYER_IP="$(curl -s https://api.ipify.org)"
if [[ -n "$DEPLOYER_IP" ]]; then
  az postgres flexible-server firewall-rule create \
    --resource-group "$RESOURCE_GROUP" \
    --name "$PSQL_NAME" \
    --rule-name "AllowDeployerIP" \
    --start-ip-address "$DEPLOYER_IP" \
    --end-ip-address "$DEPLOYER_IP" \
    --output none 2>/dev/null || true
  echo "  ✓ Firewall rule added for deployer IP (${DEPLOYER_IP})"
else
  echo "  ⚠ Could not detect public IP — you may need to add a firewall rule manually"
fi

# Allow Azure services (so AKS pods can reach the database)
az postgres flexible-server firewall-rule create \
  --resource-group "$RESOURCE_GROUP" \
  --name "$PSQL_NAME" \
  --rule-name "AllowAzureServices" \
  --start-ip-address "0.0.0.0" \
  --end-ip-address "0.0.0.0" \
  --output none 2>/dev/null || true
echo "  ✓ Firewall rule added for Azure services"

# Create the database (idempotent — no error if it already exists)
echo "  Ensuring database '${DB_NAME}' exists..."
if az postgres flexible-server db show --resource-group "$RESOURCE_GROUP" --server-name "$PSQL_NAME" --database-name "$DB_NAME" --output none 2>/dev/null; then
  echo "  ⏩ Database '${DB_NAME}' already exists — reusing"
else
  az postgres flexible-server db create \
    --resource-group "$RESOURCE_GROUP" \
    --server-name "$PSQL_NAME" \
    --database-name "$DB_NAME" \
    --output none
  echo "  ✓ Database '${DB_NAME}' created"
fi

# ── 4. Initialize Database ──────────────────────────────────────────────────
echo ""
echo "▶ Step 4/8: Initializing database with schema and seed data..."
DB_HOST="${PSQL_NAME}.postgres.database.azure.com"

# Check whether tables already exist (products is a core table present after init)
TABLE_CHECK=$(az postgres flexible-server execute \
  --name "$PSQL_NAME" \
  --admin-user "$DB_USER" \
  --admin-password "$DB_PASSWORD" \
  --database-name "$DB_NAME" \
  --querytext "SELECT COUNT(*) AS cnt FROM information_schema.tables WHERE table_schema='public' AND table_name='products';" \
  2>/dev/null || echo "")

if echo "$TABLE_CHECK" | grep -q '"cnt":.*1'; then
  echo "  ⏩ Database already initialized (tables exist) — skipping"
else
  # Use --querytext with file contents to avoid WSL/Windows path issues with --file-path
  SCHEMA_SQL="$(cat "${DB_DIR}/01-schema.sql")"
  az postgres flexible-server execute \
    --name "$PSQL_NAME" \
    --admin-user "$DB_USER" \
    --admin-password "$DB_PASSWORD" \
    --database-name "$DB_NAME" \
    --querytext "$SCHEMA_SQL"
  echo "  ✓ Schema loaded"

  SEED_SQL="$(cat "${DB_DIR}/02-data.sql")"
  az postgres flexible-server execute \
    --name "$PSQL_NAME" \
    --admin-user "$DB_USER" \
    --admin-password "$DB_PASSWORD" \
    --database-name "$DB_NAME" \
    --querytext "$SEED_SQL"
  echo "  ✓ Seed data loaded"
fi

# ── 5. AKS Cluster ──────────────────────────────────────────────────────────
echo ""
echo "▶ Step 5/8: Creating AKS cluster (this may take several minutes)..."
if az aks show --name "$AKS_NAME" --resource-group "$RESOURCE_GROUP" --output none 2>/dev/null; then
  echo "  ⏩ AKS cluster '${AKS_NAME}' already exists — reusing"
  # Ensure ACR is still attached
  az aks update \
    --resource-group "$RESOURCE_GROUP" \
    --name "$AKS_NAME" \
    --attach-acr "$ACR_NAME" \
    --output none 2>/dev/null || true
else
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
  echo "  ✓ AKS cluster '${AKS_NAME}' created"
fi

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
