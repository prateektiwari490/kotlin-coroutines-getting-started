import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat



plugins {
    id("org.jetbrains.compose") version "1.0.0-alpha1-rc3"
}


repositories {
    google()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
    implementation(compose.desktop.currentOs)
}


compose.desktop {
    application {
        mainClass = "com.knowledgespike.app.MainKt"
        args.add("-Dkotlinx.coroutines.debug")
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Echo Client"
            packageVersion = "1.0.0"
            macOS {
                dockName = "Echo Client"
            }
        }
    }
}