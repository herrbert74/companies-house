import com.babestudios.companyinfouk.buildsrc.Libs

plugins{
	id("com.babestudios.companyinfouk.plugins.android")
}

dependencies {
	implementation(Libs.baBeStudiosBase)
	implementation(Libs.AndroidX.appcompat)
	implementation(Libs.Google.material)
	implementation(Libs.Kotlin.Coroutines.core)
}

android {
	@Suppress("UnstableApiUsage")
	buildFeatures.viewBinding = true
}
