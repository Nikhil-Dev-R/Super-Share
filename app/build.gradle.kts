plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.rudraksha.supershare"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.rudraksha.supershare"
        minSdk = 29
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation("androidx.compose.material:material-icons-extended:1.5.4")

    // Koin Core features
    implementation("io.insert-koin:koin-core:3.5.3")
    // Koin Android
    implementation("io.insert-koin:koin-android:3.5.3")
    // Koin for Compose
    implementation("io.insert-koin:koin-androidx-compose:3.5.3")
    // Optional (for testing)
    testImplementation("io.insert-koin:koin-test:3.5.3")

    // Room
    implementation("androidx.room:room-runtime:2.7.1") // Added
    implementation("androidx.room:room-ktx:2.7.1") // Added
    implementation("com.google.firebase:firebase-firestore:25.1.4") // Added
    ksp("androidx.room:room-compiler:2.7.1") // Added

    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.10.1")

    // Jetpack Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.8.9")
    // Serialization (optional for stack-based navigation)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}