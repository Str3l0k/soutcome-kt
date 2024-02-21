plugins {
    alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.kotlinter) apply true
}

allprojects {
    apply {
        plugin("org.jmailen.kotlinter")
    }
}

task<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
