import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.api.plugins.quality.CheckstyleExtension

plugins {
    id("java")
    id("org.springframework.boot") version "3.4.1" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("org.sonarqube") version "6.0.1.5171"
}

group = "com.rasteronelab.lis"
version = "1.0.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

// Common configuration for all subprojects
subprojects {
    apply(plugin = "java")
    apply(plugin = "jacoco")
    apply(plugin = "checkstyle")
    apply(plugin = "io.spring.dependency-management")

    group = "com.rasteronelab.lis"
    version = "1.0.0-SNAPSHOT"

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    repositories {
        mavenCentral()
    }

    the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().apply {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:3.4.1")
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.0")
        }
    }

    dependencies {
        // MapStruct
        implementation("org.mapstruct:mapstruct:1.6.3")
        annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

        // Test dependencies
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.mockito:mockito-core")
        testImplementation("org.mockito:mockito-junit-jupiter")
        testImplementation("org.assertj:assertj-core")
        testImplementation("org.testcontainers:postgresql")
        testImplementation("org.testcontainers:junit-jupiter")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(
            listOf(
                "-parameters",
                "-Amapstruct.defaultComponentModel=spring",
                "-Amapstruct.unmappedTargetPolicy=IGNORE"
            )
        )
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            events(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
            exceptionFormat = TestExceptionFormat.FULL
            showStandardStreams = false
        }
        finalizedBy(tasks.named("jacocoTestReport"))
    }

    tasks.named<JacocoReport>("jacocoTestReport") {
        dependsOn(tasks.named("test"))
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }

    configure<CheckstyleExtension> {
        toolVersion = "10.21.1"
        configFile = rootProject.file("config/checkstyle/checkstyle.xml")
        isIgnoreFailures = true
        maxWarnings = 0
    }

    tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
        dependsOn(tasks.named("jacocoTestReport"))
        violationRules {
            rule {
                limit {
                    minimum = "0.80".toBigDecimal()
                }
            }
        }
    }
}

// Apply Spring Boot plugin to service modules (not core)
configure(
    subprojects.filter { it.name != "lis-core" }
) {
    apply(plugin = "org.springframework.boot")
}

// SonarQube — aggregate analysis at root project level
sonarqube {
    properties {
        property("sonar.projectKey", "rasteronelab-lis")
        property("sonar.projectName", "RasterOneLab LIS")
        property("sonar.projectVersion", version.toString())
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.java.coveragePlugin", "jacoco")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            subprojects.joinToString(",") { "${it.buildDir}/reports/jacoco/test/jacocoTestReport.xml" }
        )
        property(
            "sonar.junit.reportPaths",
            subprojects.joinToString(",") { "${it.buildDir}/test-results/test" }
        )
        property("sonar.exclusions", "**/generated/**,**/dto/**,**/*Mapper*.java,**/*Application.java")
    }
}
