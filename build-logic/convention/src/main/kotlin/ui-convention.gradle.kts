import com.babestudios.companyinfouk.convention.composeConfiguration
import com.babestudios.companyinfouk.convention.libs

plugins {
	id("com.android.library")
	kotlin("android")
}

android {
	composeConfiguration(this)
}

kotlin {
	compilerOptions {
		freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
		freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
	}
}

dependencies {
	"api"(project(":shared"))
	"implementation"(project(":common"))

	"implementation"(platform(libs.findLibrary("androidx.composeBom").get()))

	"implementation"(libs.findLibrary("androidx.composeFoundation").get())
	"implementation"(libs.findLibrary("androidx.composeFoundationLayout").get())
	"implementation"(libs.findLibrary("androidx.composeRuntimeSaveable").get())
	"implementation"(libs.findLibrary("androidx.composeUi").get())
	"implementation"(libs.findLibrary("androidx.composeUiGraphics").get())
	"implementation"(libs.findLibrary("androidx.composeUiText").get())
	"implementation"(libs.findLibrary("androidx.composeUiUnit").get())
	"implementation"(libs.findLibrary("androidx.composeUiTooling").get())
//	"implementation"(libs.findLibrary("androidx.composeUiToolingPreview").get())
	"implementation"(libs.findLibrary("androidx.composeMaterialIconsCore").get())
	"implementation"(libs.findLibrary("androidx.composeMaterial3").get())
	"api"(libs.findLibrary("androidx.composeRuntime").get())
	"api"(libs.findLibrary("androidx.lifecycleViewmodel").get())

	"implementation"(libs.findLibrary("baBeStudios.baseCompose").get())

	"implementation"(libs.findLibrary("kotlinx.collectionsImmutableJvm").get())
	"api"(libs.findLibrary("kotlinx.coroutinesCore").get())
	"implementation"(libs.findLibrary("kotlinResult.result").get())

	"testImplementation"(libs.findLibrary("jUnit").get())
	"testImplementation"(libs.findLibrary("kotest.assertionsShared").get())
	"testImplementation"(libs.findLibrary("kotest.assertionsCore").get())
	"testImplementation"(libs.findLibrary("kotlinx.coroutinesTest").get())
}
