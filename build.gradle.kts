plugins {
    java
    id("biz.aQute.bnd.builder") version "4.1.0"
    `maven-publish`
}

group = "org.jetbrains.kotlin"
version = "1.3.61"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8", project.version.toString()))
    implementation(kotlin("reflect", project.version.toString()))
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    jar {
        manifest {
            attributes(mapOf(
                    "Bundle-License" to "http://www.apache.org/licenses/LICENSE-2.0.txt",
                    "Bundle-SymbolicName" to "org.jetbrains.kotlin.jdk8-osgi-bundle",
                    "Export-Package" to "kotlin.*;-split-package:=merge-first;-noimport:=true",
                    "Private-Package" to "!META-INF.maven.*,META-INF.*;-split-package:=merge-first"
            ))
        }
    }
}

publishing {
    repositories {
        maven {
            name = "CitcOss"
            url = uri("https://maven1.citc.ru/content/repositories/citc-opensource")
            credentials {
                username = project.properties["mavenPublishUser.${url.host}"].toString()
                password = project.properties["mavenPublishPassword.${url.host}"].toString()
            }
        }
    }

    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
        }
    }
}
