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

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            val suffix = System.getenv("FLAVOR")
            from(files("gradle/libs.versions.$suffix.toml"))
        }
    }
}
rootProject.name = "ktorfit-koin"
include(":ktorfit-koin")
include(":lib")
include(":sample")

