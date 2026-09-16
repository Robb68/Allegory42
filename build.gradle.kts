// Plugin versions are declared once in settings.gradle.kts's pluginManagement
// block and applied per-module without a version — not `apply false` here.
// That's load-bearing, not stylistic: with `org.gradle.configureondemand=true`
// (gradle.properties), a build invocation that only touches `:core` never
// configures `:androidApp`, so the Android/Compose plugins are never
// resolved for it. Declaring them here with `apply false` would force their
// resolution on every invocation regardless of which module was requested,
// which is exactly what broke `:core:jvmTest` in an environment without
// Google Maven access before this was split out — see the Milestone 2
// report for the concrete failure.
