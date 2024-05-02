plugins {
    id("kotlin-library-config")
    id("publish-to-maven-central")
}

kotlin {
    version = project.generateVersion()
}

dependencies {
    implementation(libs.kotlin.coroutines)
}