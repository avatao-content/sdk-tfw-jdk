import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
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
