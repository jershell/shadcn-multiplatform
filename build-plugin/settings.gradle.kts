rootProject.name = "build_plugin"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        google()

        mavenCentral()
//        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://artifactory-external.vkpartner.ru/artifactory/superappkit-maven-public/")
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
