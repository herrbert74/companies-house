import com.babestudios.companyinfouk.buildsrc.Libs

plugins{
	id("com.babestudios.companyinfouk.plugins.android")
}

dependencies {
	implementation(Libs.baBeStudiosBase)
	implementation(Libs.AndroidX.appcompat)
	implementation(Libs.AndroidX.Compose.material3)
	implementation(Libs.Google.material)
	implementation(Libs.Kotlin.Coroutines.core)
	implementation(Libs.Decompose.core)
	implementation(Libs.Decompose.extensionsJetBrains)
	implementation(Libs.MviKotlin.core)
	implementation(Libs.MviKotlin.rx)
}

android {
	@Suppress("UnstableApiUsage")
	buildFeatures {
		viewBinding = true
		compose = true
	}

	composeOptions {
		kotlinCompilerExtensionVersion = "1.3.2"
	}
}
