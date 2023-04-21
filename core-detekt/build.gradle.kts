plugins{
	kotlin("jvm")
}

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

dependencies {
	api(libs.detekt.api)
	api(libs.kotlin.compiler.embeddable) //Transitive
	testImplementation(libs.detekt.test)
	testImplementation(libs.test.jUnit5.jupiterApi)
	testImplementation(libs.test.kotest.assertions.shared)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions {
		jvmTarget = "11"
	}
}

