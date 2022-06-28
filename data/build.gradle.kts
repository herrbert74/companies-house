import com.babestudios.companyinfouk.buildsrc.Libs

plugins {
	id("com.babestudios.companyinfouk.plugins.android")
	id("kotlin-parcelize")
}

val companiesHouseApiKey: String by project

android {
	buildTypes {
		all {
			buildConfigField("String", "COMPANIES_HOUSE_BASE_URL", "\"https://api.companieshouse.gov.uk\"")
			buildConfigField(
				"String", "COMPANIES_HOUSE_DOCUMENT_API_BASE_URL", "\"https://document-api.companieshouse.gov.uk\""
			)
			buildConfigField("String", "COMPANIES_HOUSE_API_KEY", companiesHouseApiKey)

			buildConfigField("String", "COMPANIES_HOUSE_SEARCH_COMPANIES_ENDPOINT", "\"search/companies\"")
			buildConfigField("String", "COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE", "\"50\"")

			buildConfigField("String", "COMPANIES_HOUSE_GET_COMPANY_ENDPOINT", "\"company/{companyNumber}\"")

			buildConfigField(
				"String", "COMPANIES_HOUSE_GET_FILING_HISTORY_ENDPOINT", "\"company/{companyNumber}/filing-history\""
			)
			buildConfigField("String", "COMPANIES_HOUSE_GET_CHARGES_ENDPOINT", "\"company/{companyNumber}/charges\"")
			buildConfigField("String", "COMPANIES_HOUSE_GET_OFFICERS_ENDPOINT", "\"company/{companyNumber}/officers\"")
			buildConfigField(
				"String", "COMPANIES_HOUSE_GET_OFFICERS_APPOINTMENTS_ENDPOINT", "\"officers/{officerId}/appointments\""
			)
			buildConfigField(
				"String", "COMPANIES_HOUSE_GET_INSOLVENCY_ENDPOINT", "\"company/{companyNumber}/insolvency\""
			)
			buildConfigField(
				"String", "COMPANIES_HOUSE_GET_PERSONS_ENDPOINT",
				"\"company/{companyNumber}/persons-with-significant-control\""
			)
			buildConfigField(
				"String", "COMPANIES_HOUSE_GET_PERSONS_INDIVIDUAL_ENDPOINT",
				"\"company/{companyNumber}/persons-with-significant-control/individual/{pscId}\""
			)
			buildConfigField(
				"String", "COMPANIES_HOUSE_GET_PERSONS_CORPORATE_ENDPOINT",
				"\"company/{companyNumber}/persons-with-significant-control/corporate-entity/{pscId}\""
			)
			buildConfigField(
				"String", "COMPANIES_HOUSE_GET_PERSONS_LEGAL_ENDPOINT",
				"\"company/{companyNumber}/persons-with-significant-control/legal-entity/{pscId}\""
			)
			buildConfigField("String", "COMPANIES_HOUSE_GET_DOCUMENT_ENDPOINT", "\"document/{documentNumber}/content\"")
		}
	}
}

dependencies {

	api(project(":common"))
	api(project(":domain"))

	api(Libs.baBeStudiosBase)
	api(Libs.SquareUp.OkHttp3.loggingInterceptor)

	implementation(Libs.Kotlin.stdLib)
	implementation(Libs.Kotlin.Coroutines.core)
	implementation(Libs.AndroidX.appcompat)
	implementation(Libs.AndroidX.coreKtx)
	implementation(Libs.Google.Dagger.core)
	implementation(Libs.Google.Dagger.Hilt.android)
	implementation(Libs.Javax.annotations)
	implementation(Libs.KotlinResult.result)
	implementation(Libs.SquareUp.Retrofit2.retrofit)
	implementation(Libs.SquareUp.Retrofit2.rxJava2Adapter)
	implementation(Libs.SquareUp.Retrofit2.converterGson)

	debugImplementation(Libs.Chucker.library)
	releaseImplementation(Libs.Chucker.noop)

	testImplementation(Libs.AndroidX.Test.Ext.jUnit)
	testImplementation(Libs.SquareUp.OkHttp3.mockWebServer)
	testImplementation(Libs.Test.MockK.core)
	testImplementation(Libs.Test.Kotest.assertions)
	testImplementation(Libs.Test.robolectric)
	testImplementation(Libs.Kotlin.Coroutines.test)
	testImplementation(Libs.Google.Dagger.Hilt.androidTesting)

	kapt(Libs.Google.Dagger.compiler)
	kapt(Libs.Google.Dagger.Hilt.compiler)

	kaptTest(Libs.Google.Dagger.compiler)

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions.freeCompilerArgs += "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
}
