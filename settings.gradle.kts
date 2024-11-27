pluginManagement {
    repositories {
        mavenCentral()
        google()
    }
}

buildscript {
    repositories {
        mavenCentral()
        google()
    }
}

rootProject.name = "ktorfit-koin"
include(":ktorfit-koin")
include(":lib")
include(":sample")

