plugins {
    application
    `kotlin-dsl`
    `java-library`
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.0.1")
    implementation("org.zeromq:jzmq:3.1.0")
}

application {
    mainClassName = "MainKt"
    applicationDefaultJvmArgs = listOf("-Djava.library.path=/usr/local/lib/")
}