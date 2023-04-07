rootProject.name = "consolemania"

include("catalog-service")
include("edge-service")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("opentelemetry-javaagent", "io.opentelemetry.javaagent:opentelemetry-javaagent:1.24.0")

            version("testcontainers", "1.18.0")
            library("testcontainers", "org.testcontainers", "testcontainers").versionRef("testcontainers")
            library("testcontainers-junit-jupiter","org.testcontainers", "junit-jupiter").versionRef("testcontainers")
            library("testcontainers-postgresql", "org.testcontainers", "postgresql").versionRef("testcontainers")

            library("urn","com.jcabi:jcabi-urn:0.9")

            version("recordBuilder", "36")
            library("record-builder-processor","io.soabase.record-builder", "record-builder-processor").versionRef("recordBuilder")
            library("record-builder-core", "io.soabase.record-builder", "record-builder-core").versionRef("recordBuilder")
        }
    }
}
