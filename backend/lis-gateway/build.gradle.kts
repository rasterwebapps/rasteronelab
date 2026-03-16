plugins {
    id("org.springframework.boot")
    id("java")
}

// Gateway uses WebFlux, not regular Servlet-based web
dependencies {
    implementation(project(":lis-core"))

    // Spring Cloud Gateway (reactive)
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")

    // Spring Security OAuth2 Resource Server
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    // Keycloak
    implementation("org.keycloak:keycloak-spring-boot-starter:24.0.5")

    // Redis for rate limiting and session
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")

    // Actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Circuit breaker
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j")
}

springBoot {
    mainClass = "com.rasteronelab.lis.gateway.LisGatewayApplication"
}
