import com.babestudios.companyinfouk.buildsrc.Libs

plugins{
	kotlin("jvm")
}

dependencies {
	implementation(Libs.Kotlin.stdLib)
	api(Libs.Detekt.api)
	testImplementation(Libs.Detekt.api)
	testImplementation(Libs.Detekt.test)
	testImplementation(Libs.Test.JUnit5.jupiterApi)
	testImplementation(Libs.Test.assertJ)
}
