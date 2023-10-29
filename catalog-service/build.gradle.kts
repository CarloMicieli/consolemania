import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("java-app-conventions")
}

group = "it.consolemania.catalog"

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    annotationProcessor(libs.record.builder.processor)
    compileOnly(libs.record.builder.core)
    implementation(libs.urn)
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.flywaydb:flyway-core")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    runtimeOnly(libs.opentelemetry.javaagent)
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

tasks.getByName<BootRun>("bootRun") {
    mainClass.set("it.consolemania.catalog.CatalogServiceApplication")
}

@Suppress("UnstableApiUsage")
testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }

        val integrationTest by registering(JvmTestSuite::class) {
            dependencies {
                implementation(project())
                implementation("org.springframework.boot:spring-boot-starter-test")

                implementation(libs.testcontainers)
                implementation(libs.testcontainers.junit.jupiter)
                implementation(libs.testcontainers.postgresql)
                implementation("io.rest-assured:rest-assured")
            }

            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                        minHeapSize = "1G"
                        maxHeapSize = "2G"

                        failFast = false

                        testLogging {
                            showStandardStreams = false
                            events(PASSED, FAILED, SKIPPED)
                            showExceptions = true
                            showCauses = true
                            showStackTraces = true
                            exceptionFormat = FULL
                        }
                    }
                }
            }
        }
    }
}

tasks.named("check") {
    dependsOn(testing.suites.named("integrationTest"))
}

tasks.named<BootBuildImage>("bootBuildImage") {
    builder.set("paketobuildpacks/builder:tiny")
    imageName.set("ghcr.io/carlomicieli/consolemania-catalog:${project.version}")
    tags.set(listOf("ghcr.io/carlomicieli/consolemania-catalog:latest"))
}
