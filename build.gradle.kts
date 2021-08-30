import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    kotlin("plugin.serialization") version "1.5.10"
    id("org.openjfx.javafxplugin") version "0.0.8"
    application
}

group = "ru.ioffe"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://repo.kotlin.link")
}

javafx {
    version = "11.0.2"
    modules = mutableListOf("javafx.controls", "javafx.graphics")
}

dependencies {
    implementation("org.apache.commons:commons-math3:3.0")
    implementation("com.charleskorn.kaml:kaml:0.35.1")
    implementation("no.tornado:tornadofx:1.7.20")
    implementation("org.jetbrains.lets-plot:lets-plot-kotlin-jvm:3.0.1")
    implementation("org.jetbrains.lets-plot:lets-plot-jfx:2.0.5-rc1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("MainKt")
    applicationDefaultJvmArgs = mutableListOf("--add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED")
}