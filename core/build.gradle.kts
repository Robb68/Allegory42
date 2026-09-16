plugins {
    kotlin("multiplatform")
    id("app.cash.sqldelight")
}

kotlin {
    // Android target is intentionally omitted until /androidApp milestone —
    // this module must compile with zero Android SDK on the classpath.
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
                implementation("app.cash.sqldelight:runtime:2.0.2")
                implementation("app.cash.sqldelight:coroutines-extensions:2.0.2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

sqldelight {
    databases {
        create("Realm444Database") {
            packageName.set("com.realm444.core.persistence")
        }
    }
}
