
val kotlinCoroutinesJavafxVersion: String by project
val tornadofxVersion: String by project
val coroutines_version: String by project
val kotlinVersion: String by project

plugins {
    application
    id("org.openjfx.javafxplugin") version "0.0.10"
}


javafx {
    version = "11.0.2"
    modules = listOf("javafx.controls")
}

application {
    mainClass.set("com.knowlegespike.fxclient.AppKt")

    applicationDefaultJvmArgs = listOf("-Dkotlinx.coroutines.debug")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:$kotlinCoroutinesJavafxVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation("no.tornado:tornadofx:$tornadofxVersion")
    implementation(kotlin("stdlib-jdk8"))
}
repositories {
    mavenCentral()
}
