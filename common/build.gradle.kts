import com.babestudios.companyinfouk.buildsrc.Libs

plugins{
	id("com.babestudios.companyinfouk.plugins.android")
}

dependencies {
	implementation(Libs.baBeStudiosBase)
	implementation(Libs.AndroidX.appcompat)
}

android {
	@Suppress("UnstableApiUsage")
	buildFeatures.viewBinding = true
}
