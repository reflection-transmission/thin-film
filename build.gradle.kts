import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    kotlin("plugin.serialization") version "1.5.10"
    id("org.openjfx.javafxplugin") version "0.0.8"
    application
}

group = "ru.ioffe"
version = "0.1"

repositories {
    mavenCentral()
    maven("https://repo.kotlin.link")
}

javafx {
    version = "11.0.2"
    modules = mutableListOf("javafx.controls", "javafx.graphics")
}

dependencies {
    implementation("com.charleskorn.kaml:kaml:0.36.0")
    implementation("no.tornado:tornadofx:1.7.20")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("com.github.ajalt.colormath:colormath:3.2.0")
    implementation("org.jetbrains.kotlinx:multik-api:0.1.1")
    implementation("org.jetbrains.kotlinx:multik-default:0.1.1")
    implementation("org.snakeyaml:snakeyaml-engine:2.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnit()
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = "ru.ioffe.thinfilm.Main"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("ru.ioffe.thinfilm.Main")
    applicationDefaultJvmArgs = mutableListOf("--add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED",
        "--add-opens=javafx.controls/javafx.scene.control=ALL-UNNAMED",
        "--add-opens=javafx.graphics/javafx.scene.graphics=ALL-UNNAMED")
}