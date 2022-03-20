import com.babestudios.companyinfouk.buildsrc.Libs

plugins{
	id("com.babestudios.companyinfouk.plugins.android")
}

dependencies {
	implementation(project(":common"))
	implementation(project(":domain"))
	implementation(Libs.AndroidX.Navigation.uiKtx)
}
