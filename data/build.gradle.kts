import com.babestudios.companyinfouk.buildsrc.Libs

plugins {
	id("com.babestudios.companyinfouk.plugins.android")
}

val companiesHouseApiKey: String by project

android {
	buildTypes {
		all {
			buildConfigField("String", "COMPANIES_HOUSE_BASE_URL", "\"https://api.companieshouse.gov.uk\"")
			buildConfigField("String", "COMPANIES_HOUSE_DOCUMENT_API_BASE_URL", "\"https://document-api.companieshouse.gov.uk\"")
			buildConfigField("String", "COMPANIES_HOUSE_API_KEY", companiesHouseApiKey)

			buildConfigField("String", "COMPANIES_HOUSE_SEARCH_COMPANIES_ENDPOINT", "\"search/companies\"")
			buildConfigField("String", "COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE", "\"100\"")

			buildConfigField("String", "COMPANIES_HOUSE_GET_COMPANY_ENDPOINT", "\"company/{companyNumber}\"")

			buildConfigField("String", "COMPANIES_HOUSE_GET_FILING_HISTORY_ENDPOINT", "\"company/{companyNumber}/filing-history\"")
			buildConfigField("String", "COMPANIES_HOUSE_GET_CHARGES_ENDPOINT", "\"company/{companyNumber}/charges\"")
			buildConfigField("String", "COMPANIES_HOUSE_GET_OFFICERS_ENDPOINT", "\"company/{companyNumber}/officers\"")
			buildConfigField("String", "COMPANIES_HOUSE_GET_OFFICERS_APPOINTMENTS_ENDPOINT", "\"officers/{officerId}/appointments\"")
			buildConfigField("String", "COMPANIES_HOUSE_GET_INSOLVENCY_ENDPOINT", "\"company/{companyNumber}/insolvency\"")
			buildConfigField("String", "COMPANIES_HOUSE_GET_PERSONS_ENDPOINT", "\"company/{companyNumber}/persons-with-significant-control\"")
			buildConfigField("String", "COMPANIES_HOUSE_GET_DOCUMENT_ENDPOINT", "\"document/{documentNumber}/content\"")
		}
	}
}

dependencies {
	api(Libs.baBeStudiosBase)
	api(project(":common"))

	implementation(Libs.Kotlin.stdLib)

	implementation(Libs.AndroidX.appcompat)
	implementation(Libs.AndroidX.coreKtx)

	implementation(Libs.SquareUp.Retrofit2.retrofit)
	implementation(Libs.SquareUp.Retrofit2.rxJava2Adapter)
	implementation(Libs.SquareUp.Retrofit2.converterGson)

	api(Libs.SquareUp.OkHttp3.loggingInterceptor)

	implementation(Libs.RxJava2.rxJava)
	implementation(Libs.RxJava2.rxAndroid)
	implementation(Libs.RxJava2.rxKotlin)

	implementation(Libs.Google.Dagger.dagger)
	implementation(Libs.Google.Firebase.analytics)

	kapt(Libs.Google.Dagger.compiler)

	debugImplementation(Libs.Chucker.library)
	releaseImplementation(Libs.Chucker.noop)

	implementation(Libs.Javax.inject)
	implementation(Libs.Javax.annotations)

	testImplementation(Libs.AndroidX.Test.Ext.jUnit)
	testImplementation(Libs.Test.mockK)
}
