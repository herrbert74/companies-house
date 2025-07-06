import com.babestudios.companyinfouk.convention.commonConfiguration
import com.babestudios.companyinfouk.convention.configureKotlinAndroid
import com.babestudios.companyinfouk.convention.libs

plugins {
	id("com.android.library")
	id("io.gitlab.arturbosch.detekt")
	id("com.autonomousapps.dependency-analysis")
	kotlin("android")
}

apply(from = project.rootProject.file("team-props/detekt/detekt.gradle"))

android {
	commonConfiguration(this)
}

kotlin {
	configureKotlinAndroid(this)
}

dependencies {
	"implementation"(libs.findLibrary("diamondedge-logging").get())
	"detekt"(libs.findLibrary("detekt.cli").get())
	"detektPlugins"(libs.findLibrary("detekt.compose").get())
}
