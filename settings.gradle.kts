rootProject.name = "WorldTools"
pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net/")
        maven("https://maven.neoforged.net/releases") {
            name = "neoforge"
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

include("common")
include("fabric")
include("forge")
include("neoforge")
