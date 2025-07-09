architectury {
    platformSetupLoomIde()
    neoForge()
}

base.archivesName.set("${base.archivesName.get()}-neoforge")

loom {
    accessWidenerPath.set(project(":common").loom.accessWidenerPath)
    /* forge {
        convertAccessWideners = true
        extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
        mixinConfig("worldtools.mixins.common.json")
    } */
}

repositories {
    maven("https://thedarkcolour.github.io/KotlinForForge/") {
        name = "KotlinForForge"
    }
    maven("https://cursemaven.com") {
        name = "Curse"
    }
    maven("https://maven.neoforged.net/releases") {
        name = "neoforge"
    }
}

val common: Configuration by configurations.creating {
    configurations.compileClasspath.get().extendsFrom(this)
    configurations.runtimeClasspath.get().extendsFrom(this)
    configurations["developmentNeoForge"].extendsFrom(this)
}

dependencies {
    neoForge("net.neoforged:neoforge:${project.properties["neoforge_version"]!!}")
    implementation("thedarkcolour:kotlinforforge-neoforge:${project.properties["kotlin_forge_version"]!!}")
    common(project(":common", configuration = "namedElements")) { isTransitive = false }
    shadowCommon(project(path = ":common", configuration = "transformProductionNeoForge")) { isTransitive = false }
    implementation(annotationProcessor("io.github.llamalad7:mixinextras-common:${project.properties["mixinextras_version"]}")!!)
    implementation(include("io.github.llamalad7:mixinextras-neoforge:${project.properties["mixinextras_version"]}")!!)
    modApi("me.shedaniel.cloth:cloth-config-neoforge:${project.properties["cloth_config_version"]}")
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/neoforge.mods.toml") {
            expand(getProperties())
            expand(mutableMapOf("version" to project.version))
        }
    }
}
