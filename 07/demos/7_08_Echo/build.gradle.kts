import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val logbackVersion: String by project
val junitVersion: String by project
val coroutines_version: String by project

plugins {
    kotlin("jvm") version "1.5.21"
}

allprojects {
    group = "com.knowledgespike"
    version = "0.1.0"

    apply(plugin = "org.jetbrains.kotlin.jvm")


    repositories {
        mavenCentral()
    }

    dependencies {


        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")

        implementation("ch.qos.logback:logback-classic:$logbackVersion")
        implementation("ch.qos.logback:logback-core:$logbackVersion")

        testImplementation(kotlin("test-junit5"))
        // testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
        testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    }

    tasks.test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    // config JVM target to 1.8 for kotlin compilation tasks
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "11"
    }

}

subprojects {
    version = "1.0"

}

project(":shared") {
}


project(":server") {
    dependencies {
        implementation(project(":shared"))
    }
}

project(":client") {
    dependencies {
        implementation(project(":shared"))
    }
}


project(":FxClient") {
    dependencies {
        implementation(project(":shared"))
    }
}

project(":DesktopClient") {
    dependencies {
        implementation(project(":shared"))
    }
}

project(":FuturesClient") {
    // dependencies {
    //     implementation(project(":shared"))
    // }
}
