package com.babestudios.companyinfouk

/**
 * Enum class to disable-enable plugins through Gradle properties
 * https://blog.dipien.com/improve-your-gradle-build-times-by-only-applying-needed-plugins-5cbe78319e17
 *
 * TLDR: Enable them by running the task like this:
 * ./gradlew dependencyUpdates -PBEN_MANES_VERSIONS_ENABLED=true
 * ./gradlew buildHealth -PDEPENDENCY_ANALYSIS_ENABLED=true
 * ./gradlew assembleMirrorDebug -PGRADLE_DOCTOR_ENABLED=true
 */
enum class PluginGradleProperty(val defaultValue: Any?) {
    BEN_MANES_VERSIONS_ENABLED(false),
    DEPENDENCY_ANALYSIS_ENABLED(false),
    GRADLE_DOCTOR_ENABLED(false),
    PROJECT_DEPENDENCY_GRAPH_ENABLED(false),
}