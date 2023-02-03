import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("java-app-conventions")
}

group = "com.example.consolemania.games"

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.liquibase:liquibase-core")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.getByName<BootRun>("bootRun") {
    mainClass.set("com.example.consolemania.games.GamesServiceApplication")
}
