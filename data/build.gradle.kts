plugins {
	id("com.babestudios.companyinfouk.plugins.android")
	id("kotlin-parcelize")
}

val companiesHouseApiKey: String by project

@Suppress("UnstableApiUsage")
android {

	namespace = "com.babestudios.companyinfouk.data"

	buildFeatures.buildConfig = true

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

	api(project(":domain"))

	implementation(libs.baBeStudiosBase) {
		exclude("androidx.navigation","navigation-fragment-ktx")
		exclude("androidx.navigation","navigation-ui-ktx")
	}
	api(libs.squareUp.okhttp3.loggingInterceptor)

	implementation(libs.kotlinx.coroutines.core)
	implementation(libs.google.dagger.core)
	implementation(libs.google.dagger.hilt.android)
	implementation(libs.kotlinResult.result)
	implementation(libs.squareUp.retrofit2.retrofit)

	debugImplementation(libs.chucker.library)
	releaseImplementation(libs.chucker.noop)

	testImplementation(libs.androidx.test.ext.jUnitKtx)
	testImplementation(libs.squareUp.okhttp3.mockWebServer)
	testImplementation(libs.kotlinx.coroutines.test)
	testImplementation(libs.test.jUnit)
	testImplementation(libs.test.robolectric)
	testImplementation(libs.test.mockk.core)
	testImplementation(libs.test.kotest.assertions)
	testImplementation(libs.google.dagger.hilt.androidTesting)

	kapt(libs.google.dagger.compiler)
	kapt(libs.google.dagger.hilt.compiler)

	kaptTest(libs.google.dagger.compiler)

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	kotlinOptions.freeCompilerArgs += "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
}
