import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED

plugins {
    id("java")
    id("io.spring.dependency-management")
    id("me.qoomon.git-versioning")
    id("com.diffplug.spotless")
}

group = "io.github.carlomicieli.consolemania"
version = "0.0.0-SNAPSHOT"
gitVersioning.apply {
    refs {
        branch("main") {
            version = "\${commit.timestamp}-\${commit.short}"
        }
        tag("v(?<version>.*)") {
            version = "\${ref.version}"
        }
    }

    rev {
        version = "\${commit.short}-SNAPSHOT"
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    withType<JavaCompile> {
        options.isIncremental = true
        options.isFork = true
        options.isFailOnError = true

        options.compilerArgs.addAll(
            arrayOf(
                "-Xlint:all",
                "-Xlint:-processing",
                "-Werror"
            )
        )
    }
}

configurations {
    all {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    implementation {
        resolutionStrategy {
            failOnVersionConflict()
        }
    }
}

configurations {
    compileClasspath {
        resolutionStrategy.activateDependencyLocking()
    }
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
    constraints {
        implementation("org.apache.logging.log4j:log4j-core") {
            version {
                strictly("[2.17, 3[")
                prefer("2.17.0")
            }
            because("CVE-2021-44228, CVE-2021-45046, CVE-2021-45105: Log4j vulnerable to remote code execution and other critical security vulnerabilities")
        }
    }

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks {
    test {
        useJUnitPlatform()

        minHeapSize = "512m"
        maxHeapSize = "1G"
        failFast = false
        ignoreFailures = true

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

spotless {
    java {
        // optional: you can specify import groups directly
        // note: you can use an empty string for all the imports you didn't specify explicitly,
        // '|' to join group without blank line, and '\\#` prefix for static imports
        importOrder("java|javax", "io.github.carlomicieli", "", "\\#io.github.carlomicieli", "\\#")
        removeUnusedImports()

        targetExclude("build/generated/aot*/**")

        palantirJavaFormat("2.9.0")

        formatAnnotations()  // fixes formatting of type annotations

        licenseHeaderFile("${project.rootDir}/.spotless/header.txt")

        toggleOffOn("fmt:off", "fmt:on")
        indentWithSpaces()
        trimTrailingWhitespace()
        endWithNewline()
    }

    kotlinGradle {
        endWithNewline()
        ktlint()
        indentWithSpaces()
        trimTrailingWhitespace()
    }
}
