# Project Scaffolding Specification — RasterOneLab LIS

## Overview

This document specifies the exact project skeleton — Gradle config, Spring Boot main classes, base entity classes, global exception handler, security config, Flyway, application.yml.

## Root Gradle: settings.gradle.kts

```kotlin
rootProject.name = "rasteronelab-lis"

include(
    "lis-core",
    "lis-patient",
    "lis-sample",
    "lis-order",
    "lis-result",
    "lis-report",
    "lis-billing",
    "lis-inventory",
    "lis-instrument",
    "lis-qc",
    "lis-admin",
    "lis-notification",
    "lis-integration",
    "lis-gateway",
    "lis-auth"
)
```

## Root Gradle: build.gradle.kts

```kotlin
plugins {
    id("org.springframework.boot") version "3.4.0" apply false
    id("io.spring.dependency-management") version "1.1.6" apply false
    id("java") apply false
    id("jacoco") apply false
    id("checkstyle") apply false
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "jacoco")

    group = "com.rasteronelab.lis"
    version = "1.0.0-SNAPSHOT"

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    repositories {
        mavenCentral()
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:3.4.0")
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.0")
        }
    }

    dependencies {
        // Common test dependencies
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.testcontainers:postgresql:1.20.4")
        testImplementation("org.testcontainers:junit-jupiter:1.20.4")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.jacocoTestReport {
        reports {
            xml.required = true
            html.required = true
        }
    }
}
```

## BaseEntity.java (lis-core)

```java
package com.rasteronelab.lis.core.domain.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "branch_id", nullable = false)
    private UUID branchId;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false, length = 100)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID(); // Replace with UUID v7 generator
        }
    }
}
```

## ApiResponse.java (lis-core)

```java
package com.rasteronelab.lis.core.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private final boolean success;
    private final String message;
    private final T data;
    private final String errorCode;
    private final LocalDateTime timestamp = LocalDateTime.now();

    private ApiResponse(boolean success, String message, T data, String errorCode) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errorCode = errorCode;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public String getErrorCode() { return errorCode; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Success", data, null);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, null);
    }

    public static <T> ApiResponse<T> error(String errorCode, String message) {
        return new ApiResponse<>(false, message, null, errorCode);
    }
}
```

## GlobalExceptionHandler (lis-core)

```java
package com.rasteronelab.lis.core.api;

import com.rasteronelab.lis.core.common.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error("LIS-SYS-001", ex.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(ApiResponse.error(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(BranchAccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleBranchDenied(BranchAccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ApiResponse.error("LIS-SEC-002", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneral(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error("LIS-SYS-003", "An internal error occurred"));
    }
}
```

## application.yml Template (per module)

```yaml
spring:
  application:
    name: lis-patient
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/lisdb}
    username: ${DB_USER:lisuser}
    password: ${DB_PASSWORD:lispassword}
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
  jpa:
    hibernate:
      ddl-auto: validate  # NEVER use create or update in production
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        default_schema: public
  flyway:
    locations: classpath:db/migration
    enabled: true
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: ${KEYCLOAK_ISSUER:http://localhost:8180/realms/rasteronelab}
  data:
    redis:
      host: ${REDIS_HOST:localhost}
      port: ${REDIS_PORT:6379}
  rabbitmq:
    host: ${RABBITMQ_HOST:localhost}
    port: ${RABBITMQ_PORT:5672}
    username: ${RABBITMQ_USER:guest}
    password: ${RABBITMQ_PASSWORD:guest}

server:
  port: ${SERVER_PORT:8081}

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      probes:
        enabled: true
      show-details: always

logging:
  level:
    com.rasteronelab: ${LOG_LEVEL:INFO}
    org.springframework.security: WARN
```

## Flyway Migrations (per module)

Flyway uses versioned SQL migration files located in `src/main/resources/db/migration/`.

### Naming Convention
```
V{YYYYMMDD_HHmm}__{description}.sql
```

### Example: `V20240115_0100__create_patient_table.sql`

```sql
-- Create patient table
CREATE TABLE patient (
    id UUID PRIMARY KEY,
    branch_id UUID NOT NULL,
    
    -- Domain columns
    name VARCHAR(200) NOT NULL,
    uhid VARCHAR(50) NOT NULL,
    
    -- Audit columns (MANDATORY)
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    
    -- Soft delete columns (MANDATORY)
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP WITH TIME ZONE
);

-- Indexes (MANDATORY)
CREATE INDEX idx_patient_branch_id ON patient(branch_id);
CREATE INDEX idx_patient_branch_deleted ON patient(branch_id, is_deleted);
CREATE INDEX idx_patient_uhid ON patient(uhid);
```

### Rules
1. File naming: `V{YYYYMMDD_HHmm}__{description}.sql` (double underscore separator)
2. Always include: id (UUID PK), branch_id, audit columns, soft delete columns
3. Always add indexes on: branch_id, (branch_id, is_deleted), foreign keys, frequently queried columns
4. Migrations are immutable — never edit a migration after it has been applied
5. Use repeatable migrations (`R__description.sql`) for views, stored procedures, or seed data
