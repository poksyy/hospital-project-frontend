plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.hospital"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.hospital"
        minSdk = 35
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // Jetpack Compose with BOM (Bill of Materials) for version alignment
    implementation(platform(libs.androidx.compose.bom.v20241100))
    implementation(libs.androidx.compose.ui.ui) // Core UI library
    implementation(libs.androidx.compose.material3.material3) // Material Design 3
    implementation(libs.androidx.compose.ui.ui.tooling.preview) // Preview tools for Compose
    implementation(libs.runtime.livedata) // LiveData support in Compose

    // Activity and Lifecycle libraries
    implementation(libs.androidx.activity.compose) // Activity integration with Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose) // ViewModel in Compose
    implementation(libs.androidx.lifecycle.runtime.ktx) // Lifecycle extensions for Kotlin

    // Core AndroidX library
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.navigation.compose) // Kotlin extensions for Android Core

    // Testing dependencies
    testImplementation(libs.junit) // Unit testing framework
    androidTestImplementation(libs.androidx.compose.ui.ui.test.junit4) // Compose UI testing
    androidTestImplementation(libs.androidx.junit) // JUnit extensions for Android testing
    androidTestImplementation(libs.androidx.espresso.core) // UI testing framework

    // Debugging tools
    debugImplementation(libs.androidx.compose.ui.ui.tooling) // UI debugging tools
    debugImplementation(libs.androidx.compose.ui.ui.test.manifest) // Manifest testing tools for Compose

    // Retrofit, Okhttp and Gson dependencies
    implementation (libs.retrofit)
    implementation(libs.okhttp)
    implementation (libs.converter.gson)

    // Navigation dependency
    implementation(libs.androidx.navigation.compose)

}

