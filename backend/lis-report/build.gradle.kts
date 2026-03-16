plugins {
    id("org.springframework.boot")
    id("java")
}

dependencies {
    implementation(project(":lis-core"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.keycloak:keycloak-spring-boot-starter:24.0.5")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.liquibase:liquibase-core")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")
    implementation("com.github.f4b6a3:uuid-creator:6.0.0")

    // PDF generation
    implementation("com.github.librepdf:openpdf:2.0.3")

    // QR Code
    implementation("com.google.zxing:core:3.5.3")
    implementation("com.google.zxing:javase:3.5.3")

    // MinIO client (8.6.0+ fixes XML Tag Value Substitution vulnerability)
    implementation("io.minio:minio:8.6.0")

    // Barcode
    implementation("net.sourceforge.barbecue:barbecue:1.5-beta1")
}

springBoot {
    mainClass = "com.rasteronelab.lis.report.LisReportApplication"
}
