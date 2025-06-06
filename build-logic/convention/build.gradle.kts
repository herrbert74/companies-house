plugins {
	`kotlin-dsl`
}

group = "com.babestudios.companyinfouk.buildlogic"

kotlin {
	jvmToolchain(21)
}

dependencies {
	//Plugins used as library references here
	implementation(libs.android.gradle.plugin)
	implementation(libs.detekt.plugin)
	implementation(libs.dependency.analysis.plugin)
	implementation(libs.jetbrains.kotlin.jvm.plugin)
	implementation(libs.kotlin.android.plugin)
	implementation(libs.ksp.plugin)
}
