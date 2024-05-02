plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-lang-config")
}

android {
    compileSdk = CoreConfig.AndroidSdk.CompileSkVersion

    defaultConfig {
        minSdk = CoreConfig.AndroidSdk.MinSdkVersion
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        targetCompatibility = JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        debug {
            isMinifyEnabled = false
        }
    }
}

dependencies {

}
