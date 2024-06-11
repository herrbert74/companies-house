plugins {
	`kotlin-dsl`
}

repositories {
	mavenCentral()
	google()
}

dependencies {

	/* Depend on the android gradle plugin, since we want to access it in our plugin */
	implementation(libs.androidGradlePlugin)

	/* Depend on the kotlin plugin, since we want to access it in our plugin */
	implementation(libs.kotlinPlugin)

	implementation(kotlin("android-extensions"))
	implementation(libs.squareUp.javaPoet) // Needed for AGP: https://github.com/google/dagger/issues/3068

	/* Depend on the default Gradle APIs since we want to build a custom plugin */
	implementation(gradleApi())
	implementation(localGroovy())
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	kotlinOptions {
		jvmTarget = "17"
	}
}
