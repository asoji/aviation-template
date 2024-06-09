plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
    alias(libs.plugins.ktor)
}

group = "gay.asoji"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://gitlab.com/api/v4/projects/26794598/packages/maven") // Aviation GitLab
}

dependencies {
    api(libs.slf4j.api)
    implementation(kotlin("reflect"))
    implementation(libs.bundles.exposed)
    implementation(libs.bundles.jda)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.ktoml)
    implementation(libs.bundles.kotlinx)
    implementation(libs.bundles.logback)
    implementation(libs.kotlin.logging)
    implementation(libs.reflections)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("gay.asoji.aviationtemplate.Main")
}

ktor {
    fatJar {
        archiveFileName.set("AviationTemplate.jar")
    }
}