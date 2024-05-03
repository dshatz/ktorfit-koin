repositories {
    mavenCentral()
    google()
}

plugins {
    alias(libs.plugins.kotlin.jvm) apply(false)
    alias(libs.plugins.kotlin.mpp) apply(false)
    alias(libs.plugins.android) apply(false)
    alias(libs.plugins.android.app) apply(false)
}