import com.babestudios.companyinfouk.buildsrc.Libs

plugins{
	id("com.babestudios.companyinfouk.plugins.android")
}

dependencies {
	implementation(project(":common"))
	implementation(Libs.AndroidX.Navigation.ktx)
}
