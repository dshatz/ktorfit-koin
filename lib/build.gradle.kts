import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.android)
    alias(libs.plugins.publish)
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


mavenPublishing {
    publishToMavenCentral(SonatypeHost.S01, automaticRelease = true)
    signAllPublications()

    val version = System.getenv("PUBLISH_VERSION")
    coordinates("com.dshatz", "ktorfit-koin-lib", version)

    pom {
        name.set(project.name)
        description.set("Generate Koin module with your Ktorfit services.")
        inceptionYear.set("2024")
        url.set("https://github.com/dshatz/ktorfit-koin/")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("dshatz")
                name.set("Daniels Šatcs")
                url.set("https://github.com/dshatz/")
            }
        }
        scm {
            url.set("https://github.com/dshatz/ktorfit-koin/")
            connection.set("scm:git:git://github.com/dshatz/ktorfit-koin.git")
            developerConnection.set("scm:git:ssh://git@github.com/dshatz/ktorfit-koin.git")
        }
    }
}
