import com.babestudios.companyinfouk.convention.configureKotlin

plugins {
	kotlin("jvm")
	//alias(libs.plugins.jetbrains.kotlin.jvm)
}

kotlin {
	configureKotlin(this)
}
