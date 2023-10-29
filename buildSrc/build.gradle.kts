plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("com.diffplug.spotless:spotless-plugin-gradle:6.22.0")
    implementation("io.spring.gradle:dependency-management-plugin:1.1.3")
    implementation("me.qoomon:gradle-git-versioning-plugin:6.4.2")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.1.5")
    implementation("com.github.ben-manes:gradle-versions-plugin:0.49.0")
    implementation("org.graalvm.buildtools.native:org.graalvm.buildtools.native.gradle.plugin:0.9.28")
}
