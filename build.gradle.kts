plugins {
    java
    id("biz.aQute.bnd.builder") version "6.4.0"
    `maven-publish`
}

group = "org.jetbrains.kotlin"
version = "1.9.10"

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
        withConvention(aQute.bnd.gradle.BundleTaskConvention::class) {
            bnd(
                mapOf(
                    "-fixupmessages" to "^Classes found in the wrong directory: \\\\{META-INF/versions/9/kotlin/reflect/jvm/internal/impl/serialization/deserialization/builtins/BuiltInsResourceLoader.class=kotlin.reflect.jvm.internal.impl.serialization.deserialization.builtins.BuiltInsResourceLoader, META-INF/versions/9/module-info.class=module-info}$",
                    "Import-Package" to "!andoid.os",
                )
            )
        }
        manifest {
            attributes(
                mapOf(
                    "Bundle-License" to "http://www.apache.org/licenses/LICENSE-2.0.txt",
                    "Bundle-SymbolicName" to "org.jetbrains.kotlin.jdk8-osgi-bundle",
                    "Export-Package" to "kotlin.*;-split-package:=merge-first;-noimport:=true",
                    "Private-Package" to "!META-INF.maven.*,META-INF.*;-split-package:=merge-first",
                )
            )
        }
    }
}

publishing {
    repositories {
        maven {
            name = "CitcOss"
            url = uri("https://maven1.citc.ru/repository/citc-opensource/")
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
