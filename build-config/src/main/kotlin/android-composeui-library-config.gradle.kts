plugins {
    id("android-library-config")
}

android {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.composeCompiler
}

dependencies {
    //    implementation(platform(libs.libAndroidxComposeBom))
    implementation(libs.composeFoundation)
    //    implementation(libs.libAndroidxComposeCompiler)
    //    implementation(libs.libAndroidxComposeMaterial3)
    //    implementation(libs.libAndroidxComposeUiToolingPreview)

    //    debugImplementation(libs.libAndroidxComposeUiTooling)
}