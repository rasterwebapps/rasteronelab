plugins {
    id("java-library")
}

dependencies {
    // Spring Data JPA (for BaseEntity, JPA annotations)
    api("org.springframework.boot:spring-boot-starter-data-jpa")

    // Spring Security (for @PreAuthorize, SecurityContextHolder)
    api("org.springframework.boot:spring-boot-starter-security")

    // OAuth2 Resource Server (for JWT-based branch validation in BranchInterceptor)
    api("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    // Spring Validation
    api("org.springframework.boot:spring-boot-starter-validation")

    // Spring Web (for ApiResponse, RestController annotations)
    api("org.springframework.boot:spring-boot-starter-web")

    // UUID v7 generator
    implementation("com.github.f4b6a3:uuid-creator:6.0.0")

    // Jackson for JSON
    api("com.fasterxml.jackson.core:jackson-databind")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
}

// No Spring Boot executable jar for core (it's a library)
tasks.named("jar") {
    enabled = true
}
