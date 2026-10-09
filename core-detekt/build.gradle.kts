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

tasks.test {
	useJUnitPlatform()
}

dependencies {
	api(libs.detekt.api)
	testImplementation(libs.detekt.test)
	testImplementation(libs.jUnit5.jupiterApi)
	testRuntimeOnly(libs.jUnit5.jupiterEngine)
	testRuntimeOnly(libs.jUnit5.platformLauncher)
	testImplementation(libs.kotest.assertionsCore)
}
