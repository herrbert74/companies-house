import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins{
	kotlin("jvm")
}

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

dependencies {
	api(libs.detekt.api)
	api(libs.kotlin.compiler.embeddable) //Transitive
	testImplementation(libs.detekt.test)
	testImplementation(libs.test.jUnit5.jupiterApi)
	testImplementation(libs.test.kotest.assertions.shared)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions{
		jvmTarget.set(JvmTarget.JVM_21)
	}
}

