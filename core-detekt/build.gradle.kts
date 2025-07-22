import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	kotlin("jvm")
}

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

kotlin {
	compilerOptions {
		jvmTarget = JvmTarget.JVM_21
	}
}

dependencies {
	api(libs.detekt.api)
	api(libs.kotlin.compilerEmbeddable) // Transitive
	testImplementation(libs.detekt.test)
	testImplementation(libs.jUnit5.jupiterApi)
	testImplementation(libs.kotest.assertionsShared)
}
