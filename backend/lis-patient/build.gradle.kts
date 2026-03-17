plugins {
    id("org.springframework.boot")
    id("java")
}

dependencies {
    implementation(project(":lis-core"))

    // Spring Boot starters
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Keycloak
    implementation("org.keycloak:keycloak-spring-boot-starter:24.0.5")

    // Database
    runtimeOnly("org.postgresql:postgresql")

    // Flyway
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // RabbitMQ
    implementation("org.springframework.boot:spring-boot-starter-amqp")

    // OpenAPI docs
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

    // UUID Creator
    implementation("com.github.f4b6a3:uuid-creator:6.0.0")
}

springBoot {
    mainClass = "com.rasteronelab.lis.patient.LisPatientApplication"
}
