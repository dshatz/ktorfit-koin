buildscript {
    repositories {
        mavenCentral()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            val suffix = "2.0-RC1"/*System.getenv("flavor")*/
            from(files("gradle/libs.versions.$suffix.toml"))
        }
    }
}
rootProject.name = "ktorfit-koin"
include(":ktorfit-koin")
include(":sample")

