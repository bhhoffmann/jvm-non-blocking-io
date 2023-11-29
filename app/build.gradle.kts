
plugins {
    kotlin("jvm") version "1.9.20"

    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

kotlin {
    jvmToolchain(21)
}

application {
    // Define the main class for the application.
    mainClass.set("no.bhhoffmann.nio.examples.AppKt")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    implementation("io.netty:netty-all:4.1.100.Final")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}