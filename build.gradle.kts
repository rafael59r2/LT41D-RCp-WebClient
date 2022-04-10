import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

// Plugins to be applied
plugins {
    // Applies java plugin to build.gradle.kts
    java

    // Applies kotlin(jvm) plugin to build.gradle.kts
    kotlin("jvm") version "1.6.20"

    // Applies shadow plugin to build.gradle.kts
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

// Sets project group
group = "org.github.rafael59r2.isel"
// Sets project version
version = "1.0-SNAPSHOT"

// Java
java {
    // Sets java compatibility to 1.8 version
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

// Repositories
repositories {
    // Adds maven central repository to repositories
    mavenCentral()
}

// Dependencies
dependencies {
    // Implements kotlin-stdlib
    implementation(kotlin("stdlib"))

    // Implements a web server for unit tests
    testImplementation("io.ktor:ktor-server-core:1.6.8")
    testImplementation("io.ktor:ktor-server-netty:1.6.8")
    testImplementation("ch.qos.logback:logback-classic:1.2.11")

    // Implements kotlin-test for unit tests
    testImplementation(kotlin("test"))

    // Implements JUnit 5 for unit tests
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks{
    // Test task
    test{
        // Uses JUnit 5 platform for unit tests
        useJUnitPlatform()
    }
    // Shadow jar task
    named<ShadowJar>("shadowJar"){
        // Sets the output archive name to default
        archiveBaseName.set(project.name)
        archiveClassifier.set("")

        mergeServiceFiles()

        // Jar file manifest
        manifest{
            // Sets main class to be executed (org.github.rafael59r2.isel.webclient.LauncherKt)
            attributes(mapOf("Main-Class" to "org.github.rafael59r2.isel.webclient.LauncherKt"))
        }
    }

    // Build task
    build{
        // Executes shadow jar task after build task
        dependsOn("shadowJar")
    }

}