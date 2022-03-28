plugins {
    application
}

application {
    mainClass.set("com.knowledgespike.serverasync.AsyncEchoServerCallackSuspendedKt")
}


dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

repositories {
    mavenCentral()
}
