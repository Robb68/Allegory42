rootProject.name = "realm444"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        kotlin("multiplatform") version "2.0.21"
        kotlin("android") version "2.0.21"
        id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"
        id("com.android.application") version "8.5.2"
        id("app.cash.sqldelight") version "2.0.2"
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include(":core")
include(":androidApp")
