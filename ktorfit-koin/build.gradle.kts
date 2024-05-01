import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.publish)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.23-1.0.20")
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.S01, automaticRelease = true)
    signAllPublications()

    coordinates("com.dshatz", "ktorfit-koin", "1.0.0")

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