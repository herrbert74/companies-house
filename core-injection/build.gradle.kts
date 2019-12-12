import com.babestudios.companyinfouk.buildsrc.Libs

plugins{
	id("com.babestudios.companyinfouk.plugins.android")
}

dependencies {
	api(project(":navigation"))
	api(project(":data"))

	implementation(Libs.Google.Dagger.dagger)
	kapt(Libs.Google.Dagger.compiler)

	implementation(Libs.SquareUp.Retrofit2.retrofit)
	implementation(Libs.Google.gson)
	implementation(Libs.Google.Firebase.analytics)

	implementation(Libs.Javax.inject)
	kapt(Libs.Javax.annotations)
}
