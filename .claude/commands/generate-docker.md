# Generate Docker Configuration — /generate-docker

Generate Docker configuration for a new service or module.

## Usage
```
/generate-docker {service} {port} {type}
```

Types: `backend-module`, `frontend`, `microservice`

## Backend Module Dockerfile (Multi-Stage)

```dockerfile
# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle/ gradle/
RUN ./gradlew dependencies --no-daemon
COPY . .
RUN ./gradlew :lis-patient:bootJar --no-daemon -x test

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Non-root user for security
RUN addgroup -S lisapp && adduser -S lisapp -G lisapp
USER lisapp

COPY --from=builder /app/lis-patient/build/libs/*.jar app.jar

EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget -q --spider http://localhost:8081/actuator/health || exit 1

ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar"]
```

## docker-compose Service Entry

```yaml
lis-patient:
  build:
    context: ../../backend
    dockerfile: ../infrastructure/docker/Dockerfile.backend
    args:
      MODULE: lis-patient
      PORT: 8081
  image: rasteronelab/lis-patient:${VERSION:-latest}
  container_name: lis-patient
  ports:
    - "8081:8081"
  environment:
    - SPRING_PROFILES_ACTIVE=docker
    - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/lisdb
    - SPRING_DATASOURCE_USERNAME=${DB_USER}
    - SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}
    - SPRING_REDIS_HOST=redis
    - KEYCLOAK_AUTH_SERVER_URL=http://keycloak:8080
  depends_on:
    postgres:
      condition: service_healthy
    redis:
      condition: service_healthy
    keycloak:
      condition: service_healthy
  networks:
    - lis-network
  restart: unless-stopped
  healthcheck:
    test: ["CMD", "wget", "-q", "--spider", "http://localhost:8081/actuator/health"]
    interval: 30s
    timeout: 10s
    retries: 3
    start_period: 60s
```

## Health Check Endpoint
```java
@RestController
@RequestMapping("/actuator")
public class HealthController {
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}
```
