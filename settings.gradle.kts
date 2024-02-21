pluginManagement {
    includeBuild("build-config")

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "soutcome-kt"

// modules
include(":soutcome-test")
include(":soutcome-lib")
