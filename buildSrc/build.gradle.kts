plugins {
	`kotlin-dsl`
}

repositories {
	mavenCentral()
	google()
}

dependencies {
	/* Depend on the android gradle plugin, since we want to access it in our plugin */
	//implementation("com.android.tools.build:gradle:7.4.0-rc01")
	implementation("com.android.tools.build:gradle:7.3.0") //For IJ plugin runIde

	/* Depend on the kotlin plugin, since we want to access it in our plugin */
	implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")

	implementation(kotlin("android-extensions"))

	/* Depend on the default Gradle APIs since we want to build a custom plugin */
	implementation(gradleApi())
	implementation(localGroovy())
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	kotlinOptions {
		jvmTarget = "11"
	}
}
