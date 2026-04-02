plugins {
    id("org.springframework.boot")
    id("java")
}

dependencies {
    implementation(project(":lis-core"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

    // Keycloak admin client (to manage users, roles programmatically)
    implementation("org.keycloak:keycloak-admin-client:24.0.5")

    // Spring Security OAuth2
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    // Redis for token cache + connection pool support
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.apache.commons:commons-pool2")

    // Spring Security Test (for jwt() MockMvc post-processor)
    testImplementation("org.springframework.security:spring-security-test")
}

springBoot {
    mainClass = "com.rasteronelab.lis.auth.LisAuthApplication"
}
