plugins {
//    kotlin("jvm")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.serialization)
}

group = "com.dshatz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.koin.core)
    implementation(libs.koin.annotations)
    implementation(libs.ktorfit)
    implementation(libs.bundles.ktor)
    ksp(libs.koin.kspCompiler)
    ksp(libs.ktorfit.ksp)
    ksp(project(":ktorfit-koin"))
    testImplementation(kotlin("test"))
    testImplementation(libs.koin.test)
    testImplementation(libs.coroutines.test)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}