import com.babestudios.companyinfouk.buildsrc.Libs

plugins{
	id("com.babestudios.companyinfouk.plugins.android")
}

dependencies {
	implementation(Libs.baBeStudiosBase)
	implementation(Libs.Google.gson)
	implementation(Libs.RxJava2.rxJava)
	implementation(Libs.SquareUp.OkHttp3.okHttp)
}

android {
	@Suppress("UnstableApiUsage")
	buildFeatures.viewBinding = true
}
