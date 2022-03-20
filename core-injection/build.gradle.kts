import com.babestudios.companyinfouk.buildsrc.Libs

plugins{
	id("com.babestudios.companyinfouk.plugins.android")
}

dependencies {
	api(project(":navigation"))
	api(project(":data"))

	implementation(Libs.Google.Dagger.core)
	kapt(Libs.Google.Dagger.compiler)

	implementation(Libs.SquareUp.Retrofit2.retrofit)
	implementation(Libs.Google.gson)

	implementation(Libs.Javax.inject)
	kapt(Libs.Javax.annotations)
}
