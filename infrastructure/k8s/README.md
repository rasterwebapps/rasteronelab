# Kubernetes Manifests — RasterOneLab LIS

This directory contains Kubernetes deployment manifests for production deployment.

## Namespace Structure

```
lis-prod/          # Production namespace
lis-staging/       # Staging namespace
lis-dev/           # Development namespace
```

## Services

| Service | Port | Replicas |
|---------|------|----------|
| lis-gateway | 8080 | 2+ |
| lis-patient | 8081 | 2+ |
| lis-order | 8082 | 2+ |
| lis-sample | 8083 | 2+ |
| lis-result | 8084 | 2+ |
| lis-report | 8085 | 1+ |
| lis-billing | 8086 | 2+ |
| lis-admin | 8090 | 1+ |

## Quick Deploy

```bash
# Create namespace
kubectl create namespace lis-prod

# Apply secrets (never commit secrets to git!)
kubectl apply -f secrets/ -n lis-prod

# Deploy all services
kubectl apply -f deployments/ -n lis-prod
kubectl apply -f services/ -n lis-prod

# Check rollout status
kubectl rollout status deployment/lis-gateway -n lis-prod
```

## Ingress

Use NGINX Ingress Controller or AWS ALB Ingress Controller:

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: lis-ingress
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
spec:
  rules:
  - host: lis.yourdomain.com
    http:
      paths:
      - path: /api
        pathType: Prefix
        backend:
          service:
            name: lis-gateway
            port: { number: 8080 }
      - path: /
        pathType: Prefix
        backend:
          service:
            name: lis-frontend
            port: { number: 80 }
```

## Scaling

```bash
# Scale gateway for high traffic
kubectl scale deployment lis-gateway --replicas=3 -n lis-prod

# Auto-scaling (HPA)
kubectl autoscale deployment lis-patient --min=2 --max=10 --cpu-percent=70 -n lis-prod
```
