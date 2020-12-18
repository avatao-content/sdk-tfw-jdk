import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.withType

plugins {
    application
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
    id("maven-publish")
    id("signing")
}

repositories {
    mavenCentral()
    jcenter()
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.0.1")
    implementation("org.zeromq:jeromq:0.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0-RC2")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")

    testImplementation("org.mockito:mockito-core:3.5.13")
    testImplementation(platform("org.junit:junit-bom:5.7.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
    systemProperty("java.library.path", "/usr/local/lib/")
    testLogging {
        events("passed", "skipped", "failed")
    }
}

// config JVM target to 1.8 for kotlin compilation tasks
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "com.avatao.tfw.sdk.MainKt"
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "com.avatao.tfw.sdk.MainKt"
    }
}

val fatJar = task("fatJar", type = Jar::class) {
    manifest {
        attributes["Main-Class"] = "com.avatao.tfw.sdk.MainKt"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks.jar.get() as CopySpec)
}

tasks {
    "assemble" {
        dependsOn(fatJar)
    }
}

publishing {
    publishWith(
        project = project,
        module = "tfw.sdk.jvm",
        desc = "Avatao Content SDK for TFW."
    )
}

signing {
    isRequired = false
    sign(publishing.publications)
}

fun PublishingExtension.publishWith(
    project: Project,
    module: String,
    desc: String
) {

    with(project) {

        val emptyJavadocJar by tasks.registering(Jar::class) {
            archiveClassifier.set("javadoc")
        }

        val POM_URL: String by project
        val POM_SCM_URL: String by project
        val POM_SCM_CONNECTION: String by project
        val POM_SCM_DEV_CONNECTION: String by project
        val POM_LICENCE_NAME: String by project
        val POM_LICENCE_URL: String by project
        val POM_LICENCE_DIST: String by project
        val POM_DEVELOPER_ID: String by project
        val POM_DEVELOPER_NAME: String by project
        val POM_DEVELOPER_EMAIL: String by project
        val POM_DEVELOPER_ORGANIZATION: String by project
        val POM_DEVELOPER_ORGANIZATION_URL: String by project

        publications.withType<MavenPublication>().all {

            pom {

                name.set(module)
                description.set(desc)
                url.set(POM_URL)

                scm {
                    url.set(POM_SCM_URL)
                    connection.set(POM_SCM_CONNECTION)
                    developerConnection.set(POM_SCM_DEV_CONNECTION)
                }

                licenses {
                    license {
                        name.set(POM_LICENCE_NAME)
                        url.set(POM_LICENCE_URL)
                        distribution.set(POM_LICENCE_DIST)
                    }
                }

                developers {
                    developer {
                        id.set(POM_DEVELOPER_ID)
                        name.set(POM_DEVELOPER_NAME)
                        email.set(POM_DEVELOPER_EMAIL)
                        organization.set(POM_DEVELOPER_ORGANIZATION)
                        organizationUrl.set(POM_DEVELOPER_ORGANIZATION_URL)
                    }
                }
            }

            artifact(emptyJavadocJar.get())
        }

        repositories {

            val sonatypeUsername = System.getenv("SONATYPE_USERNAME") ?: ""
            val sonatypePassword = System.getenv("SONATYPE_PASSWORD") ?: ""

            maven {
                url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = if (sonatypeUsername.isBlank()) "" else sonatypeUsername
                    password = if (sonatypePassword.isBlank()) "" else sonatypePassword
                }
            }
        }
    }
}
