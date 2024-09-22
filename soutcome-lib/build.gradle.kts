plugins {
    id("kotlin-library-config")
    id("publish-to-maven-central")
}

kotlin {
    println("Kotlin version: ${libs.versions.kotlin.lang.get()}")
    println("Project version: ${project.generateVersion()}")

    version = project.generateVersion()
}

dependencies {
    implementation(libs.kotlin.coroutines)
}