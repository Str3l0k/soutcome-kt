plugins {
    id("kotlin-library-config")
}

dependencies {
    implementation(project(":soutcome-lib"))
    implementation(libs.kotlin.coroutines)

    testImplementation(platform("org.junit:junit-bom:5.5.0"))
    testImplementation(libs.kotest.runner)
}