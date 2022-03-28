rootProject.name = "echo"

include("server")
include("client")
include("FxClient")
include("FuturesClient")
include("DesktopClient")
include("shared")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }
}
