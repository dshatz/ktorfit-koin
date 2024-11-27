plugins {
    kotlin("multiplatform")
//    alias(libs.plugins.android.app)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.serialization)
}

group = "com.dshatz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

kotlin {
    applyDefaultHierarchyTemplate()
    jvmToolchain(17)
    jvm()
//    linuxX64()
//    androidTarget()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.koin.core)
                implementation(libs.koin.annotations)
                implementation(libs.ktorfit)
                implementation(libs.bundles.ktor)
                implementation(project(":lib"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.koin.test)
                implementation(libs.coroutines.test)
            }
        }

        /*val androidMain by getting {
            dependencies {
                implementation(libs.koin.android)
            }
        }*/
    }
}

dependencies {
//    add("kspCommonMainMetadata", libs.koin.kspCompiler)
    add("kspJvm", libs.koin.kspCompiler)
//    add("kspLinuxX64", libs.koin.kspCompiler)

//    add("kspCommonMainMetadata", libs.ktorfit.ksp)
    add("kspJvm", libs.ktorfit.ksp)
//    add("kspLinuxX64", libs.ktorfit.ksp)

//    add("kspCommonMainMetadata", project(":ktorfit-koin"))
    add("kspJvm", project(":ktorfit-koin"))
//    add("kspLinuxX64", project(":ktorfit-koin"))
}

/*tasks.test {
    useJUnitPlatform()
}*/

/*android {
    namespace = "com.dshatz.ktorfitkoin"
    defaultConfig {
        compileSdk = 34
        minSdk = 21
    }
}*/

/*
tasks.test {
    useJUnitPlatform()
}*/

ksp {
    arg("KOIN_CONFIG_CHECK","true")
}
