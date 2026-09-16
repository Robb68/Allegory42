plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.realm444.android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.realm444.android"
        // Locked (Manus Build Prompt Section 11): minimum API level 26.
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "0.1.0-milestone2"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        // Matches :core's jvmToolchain(17) — see the comment there.
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    // /androidApp is the only module allowed to import /core AND render (Section 3).
    implementation(project(":core"))

    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.navigation:navigation-compose:2.8.0")

    // /androidApp/persistence's one job: the platform SqlDriver (Section 3).
    implementation("app.cash.sqldelight:android-driver:2.0.2")

    debugImplementation("androidx.compose.ui:ui-tooling")
}
