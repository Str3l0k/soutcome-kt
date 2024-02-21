plugins {
    id("kotlin-library-config")
}

kotlin {
    version = "1.0.0"
}

dependencies {
    implementation(libs.kotlin.coroutines)
}