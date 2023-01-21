buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    kotlin("multiplatform") version "1.8.0"
    id("io.kotest.multiplatform") version "5.5.4"
}

repositories {
    mavenCentral()
}

dependencies {
    commonTestImplementation("io.kotest:kotest-framework-engine:5.5.4")
    commonMainImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}

kotlin {
    jvm()

    sourceSets {
        @Suppress("UNUSED_VARIABLE") val jvmTest by getting {
            dependencies {
                implementation("io.kotest:kotest-framework-engine:5.5.4")
                implementation("io.kotest:kotest-runner-junit5-jvm:5.5.4")
            }
        }
    }
}

tasks.withType<Test>() {
    useJUnitPlatform()
}