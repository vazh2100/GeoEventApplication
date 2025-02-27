pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("com.autonomousapps.build-health") version "2.5.0"
        id("com.android.application") version "8.7.2" apply false
        id("org.jetbrains.kotlin.android") version "2.0.21" apply false
        id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "GeoEventApplication"
include(":0_app")
include(":1_core_a")
include(":1_network")
include(":2_events")
include(":1_geolocation")
