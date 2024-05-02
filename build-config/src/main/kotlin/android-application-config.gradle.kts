plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-lang-config")
}

android {
    compileSdk = CoreConfig.AndroidSdk.CompileSkVersion

    defaultConfig {
        minSdk = CoreConfig.AndroidSdk.MinSdkVersion
        targetSdk = CoreConfig.AndroidSdk.CompileSkVersion
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        debug {
            isMinifyEnabled = false
            isDebuggable = true
        }
    }
}
