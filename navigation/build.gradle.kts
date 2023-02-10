import com.babestudios.companyinfouk.buildsrc.Libs

plugins {
	id("com.babestudios.companyinfouk.plugins.android")
	id("androidx.navigation.safeargs.kotlin")
}

android.namespace = "com.babestudios.companyinfouk.navigation"

dependencies {
	implementation(project(":domain"))
	implementation(Libs.AndroidX.Navigation.fragment)
	implementation(Libs.AndroidX.Navigation.uiKtx)
}
