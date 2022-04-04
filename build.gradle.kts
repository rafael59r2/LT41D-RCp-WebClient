plugins {
    kotlin("jvm") version "1.6.0"
    java
}

group = "org.github.rafael59r2.isel"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

    testImplementation("io.ktor:ktor-server-core:1.6.8")
    testImplementation("io.ktor:ktor-server-netty:1.6.8")
    testImplementation("ch.qos.logback:logback-classic:1.2.5")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}