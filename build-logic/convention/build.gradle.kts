plugins {
	`kotlin-dsl`
}

group = "com.babestudios.companyinfouk.buildlogic"

kotlin {
	jvmToolchain(21)
}

dependencies {
	implementation(libs.kotlinMultiplatform.plugin)
	implementation(libs.android.kotlin.multiplatform.library.gradle.plugin)
}
