plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.build.gradle)
    implementation(libs.build.kotlin.plugin)
    implementation(libs.build.maven.central.publish.plugin)
}

tasks.test {
    useJUnitPlatform()
}
