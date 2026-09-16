plugins {
    kotlin("multiplatform")
    id("app.cash.sqldelight")
}
// Versions for both plugins come from settings.gradle.kts's pluginManagement.

kotlin {
    // Android target is intentionally omitted until /androidApp milestone —
    // this module must compile with zero Android SDK on the classpath.
    jvm {
        // Matches /androidApp's compileOptions/kotlinOptions target (17) —
        // both must agree or the class files :androidApp consumes from
        // :core as a plain project dependency risk a bytecode-version
        // mismatch at dex time. A bytecode target, not a toolchain request:
        // this compiles with whatever JDK Gradle is running on (no separate
        // JDK 17 install needed — none is available in this sandbox).
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions.jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                // api, not implementation: Marker.timestamp/targetDate are
                // public Instant/LocalDate, Realm444Database is a public
                // Transacter, and the repository interfaces expose Flow<...>
                // and CoroutineDispatcher params — all part of /core's
                // public surface. implementation() here compiled fine in
                // this module but left every one of those types
                // inaccessible to /androidApp ("Cannot access class ...
                // Check your module classpath"), since implementation
                // dependencies don't propagate to consumers.
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
                api("app.cash.sqldelight:runtime:2.0.2")
                // Only used internally (SqlDelightRepositories.kt's
                // .asFlow()/mapToList) — nothing public exposes its types.
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
