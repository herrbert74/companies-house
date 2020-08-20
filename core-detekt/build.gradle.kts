import com.babestudios.companyinfouk.buildsrc.Libs

plugins{
	kotlin("jvm")
}

dependencies {
	implementation(Libs.Kotlin.stdLib)
	api(Libs.Detekt.api)
	testImplementation(Libs.Detekt.api)
	testImplementation(Libs.Detekt.test)
	testImplementation(Libs.Test.jUnit5)
	testImplementation(Libs.Test.assertJ)
}
