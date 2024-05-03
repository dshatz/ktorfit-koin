plugins {
    kotlin("multiplatform")
    alias(libs.plugins.android)
}

repositories {
    mavenCentral()
    google()
}

kotlin {
    jvmToolchain(17)
    applyDefaultHierarchyTemplate()
    jvm()
    iosArm64()
    iosSimulatorArm64()
    iosX64()
    macosArm64()
    macosX64()
    mingwX64()
    linuxArm64()
    linuxX64()
    js(IR) {
        binaries.library()
        browser()
    }
    wasmJs() {
        binaries.library()
        browser()
    }
    androidTarget()
}

android {
    namespace = "com.dshatz.ktorfitkoin"
    compileSdk = 34
    defaultConfig {
    }
}
