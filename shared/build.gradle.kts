
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("maven-publish")
    id("org.jetbrains.dokka") version "2.2.0"
}

group = providers.gradleProperty("GROUP").get()
version = providers.gradleProperty("VERSION_NAME").get()

kotlin {
    androidTarget {
        publishLibraryVariants("release")
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    iosArm64()
    iosSimulatorArm64()

    linuxX64()
    mingwX64()

    sourceSets {
        commonMain.dependencies {
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.rekcode.yahtzee.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

tasks.register<Jar>("dokkaHtmlJar") {
    dependsOn("dokkaGenerate")
    archiveClassifier.set("javadoc")
    from(layout.buildDirectory.dir("dokka/html"))
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri(
                "https://maven.pkg.github.com/${
                    providers.gradleProperty("GITHUB_PACKAGES_OWNER").get()
                }/${
                    providers.gradleProperty("GITHUB_PACKAGES_REPOSITORY").get()
                }"
            )
            credentials {
                username = project.findProperty("gpr.user") as String?
                    ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String?
                    ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

gradle.projectsEvaluated {
    publishing {
        publications.withType<org.gradle.api.publish.maven.MavenPublication>().all {
            val baseName = providers.gradleProperty("POM_ARTIFACT_ID").get()

            artifactId = when (name) {
                "kotlinMultiplatform" -> baseName
                else -> "$baseName-$name"
            }
            pom {
                name.set(providers.gradleProperty("POM_ARTIFACT_ID"))
                description.set("Kotlin Multiplatform Yahtzee game engine (logic-only, platform-independent)")
                url.set("https://github.com/${providers.gradleProperty("GITHUB_PACKAGES_OWNER").get()}/${providers.gradleProperty("GITHUB_PACKAGES_REPOSITORY").get()}")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("rek-65")
                        name.set("Robert Kennedy")
                    }
                }

                scm {
                    url.set("https://github.com/${providers.gradleProperty("GITHUB_PACKAGES_OWNER").get()}/${providers.gradleProperty("GITHUB_PACKAGES_REPOSITORY").get()}")
                }
            }
        }
    }
}

tasks.withType<Jar>().configureEach {
    archiveBaseName.set(providers.gradleProperty("POM_ARTIFACT_ID").get())
    archiveVersion.set(project.version.toString())
}