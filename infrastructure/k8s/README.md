# Kubernetes Manifests — RasterOneLab LIS

This directory contains Kubernetes deployment manifests for production deployment.

## Namespace Structure

```
lis-prod/          # Production namespace
lis-staging/       # Staging namespace
lis-dev/           # Development namespace
```

## Directory Layout

```
k8s/
├── namespaces/        # Namespace definitions (lis-dev, lis-staging, lis-prod)
├── secrets/           # Secret templates — fill and apply manually, NEVER commit real values
├── configmaps/        # Non-secret configuration shared across services
├── deployments/       # One Deployment YAML per service (15 total)
├── services/          # One ClusterIP Service YAML per service (15 total)
├── ingress/           # NGINX Ingress Controller routing (API + SPA + WebSocket)
└── hpa/               # HorizontalPodAutoscaler for high-traffic services
```

## Services

| Service | Port | Min Replicas | Max Replicas (HPA) |
|---------|------|-------------|---------------------|
| lis-gateway | 8080 | 2 | 8 |
| lis-patient | 8081 | 2 | 10 |
| lis-order | 8082 | 2 | 8 |
| lis-sample | 8083 | 2 | 6 |
| lis-result | 8084 | 2 | 6 |
| lis-report | 8085 | 1 | — |
| lis-billing | 8086 | 2 | 6 |
| lis-inventory | 8087 | 1 | — |
| lis-instrument | 8088 | 1 | — |
| lis-qc | 8089 | 1 | — |
| lis-admin | 8090 | 1 | — |
| lis-notification | 8091 | 1 | — |
| lis-integration | 8092 | 1 | — |
| lis-auth | 8093 | 2 | 6 |
| lis-frontend | 80 | 2 | 4 |

## Prerequisites

```bash
# 1. NGINX Ingress Controller
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.12.0/deploy/static/provider/cloud/deploy.yaml

# 2. cert-manager (for TLS via Let's Encrypt)
kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.17.0/cert-manager.yaml

# 3. metrics-server (for HPA)
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
```

## Quick Deploy (Production)

```bash
# Step 1 — Create namespace
kubectl apply -f namespaces/namespaces.yaml

# Step 2 — Apply secrets (fill in secrets-template.yaml first, NEVER commit real values)
#   SECRETFILE=$(mktemp -p "$HOME" --suffix=.yaml)
#   chmod 600 "$SECRETFILE"
#   cp secrets/secrets-template.yaml "$SECRETFILE"
#   vi "$SECRETFILE"   # replace all <base64-encoded-value> placeholders
#   kubectl apply -f "$SECRETFILE" -n lis-prod
#   shred -u "$SECRETFILE"   # securely delete the file after applying

# Step 3 — Apply configmaps
kubectl apply -f configmaps/ -n lis-prod

# Step 4 — Deploy all services
kubectl apply -f deployments/ -n lis-prod
kubectl apply -f services/ -n lis-prod

# Step 5 — Apply ingress (update host in ingress/ingress.yaml first)
kubectl apply -f ingress/ -n lis-prod

# Step 6 — Apply HPA
kubectl apply -f hpa/ -n lis-prod

# Step 7 — Check rollout status
for svc in lis-gateway lis-patient lis-order lis-sample lis-result lis-report \
           lis-billing lis-inventory lis-instrument lis-qc lis-admin \
           lis-notification lis-integration lis-auth lis-frontend; do
  kubectl rollout status deployment/$svc -n lis-prod
done
```

## Ingress

The ingress routes:
- `/api/*` → `lis-gateway:8080`
- `/ws/*` → `lis-gateway:8080` (WebSocket)
- `/swagger-ui/*` → `lis-gateway:8080`
- `/*` → `lis-frontend:80` (Angular SPA, catch-all)

Edit `ingress/ingress.yaml` and replace `lis.yourdomain.com` with your actual domain before applying.
Uncomment the `tls:` block once cert-manager is installed and DNS is configured.

## Scaling

```bash
# Manually scale gateway for high traffic
kubectl scale deployment lis-gateway --replicas=5 -n lis-prod

# Watch HPA status
kubectl get hpa -n lis-prod -w
```

## Jenkins Pipeline Integration

The Jenkinsfile's `Deploy` stage uses `kubectl set image` to roll out new images after a build.
The deployments must already exist in the target namespace (apply this directory first).
The Jenkins service account must have `patch` permission on `deployments` in the target namespace.

```bash
# Create a minimal Jenkins service account (example — scope to your namespace)
kubectl create serviceaccount jenkins -n lis-prod
kubectl create clusterrolebinding jenkins-deploy \
  --clusterrole=edit \
  --serviceaccount=lis-prod:jenkins \
  -n lis-prod
```

