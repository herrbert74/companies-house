import com.babestudios.companyinfouk.buildsrc.Libs

plugins {
	id("com.babestudios.companyinfouk.plugins.android")
	id("jacoco")
	id("com.google.firebase.crashlytics")
	id("com.google.gms.google-services")
	id("org.jetbrains.kotlin.plugin.allopen")
	id("com.google.devtools.ksp") version "1.6.21-1.0.5"
	id("dagger.hilt.android.plugin")
}

android {
	defaultConfig {
		applicationId = "com.babestudios.companyinfouk"
		versionCode = 10
		versionName = "2.0"
		vectorDrawables.useSupportLibrary = true
		testInstrumentationRunner = "com.babestudios.companyinfouk.CompaniesHouseAndroidJUnitRunner"
	}
	signingConfigs {

		create("release") {
			storeFile = file("CH_RELEASE_STORE_FILE")
			storePassword = properties["CH_RELEASE_STORE_PASSWORD"].toString()
			keyAlias = properties["CH_RELEASE_KEY_ALIAS"].toString()
			keyPassword = properties["CH_RELEASE_KEY_PASSWORD"].toString()
		}
	}
	buildTypes {
		getByName("release") {
			//testCoverageEnabled true
			isDebuggable = false
			isMinifyEnabled = true
			signingConfig = signingConfigs.getByName("release")
			proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
		}
		getByName("debug") {
			isTestCoverageEnabled = true
			isMinifyEnabled = false
		}
	}
	testOptions {
		unitTests.isIncludeAndroidResources = true
	}

	applicationVariants.all {
		val isTest: Boolean = gradle.startParameter.taskNames.find {
			it.contains("test") || it.contains(
				"Test"
			)
		} != null
		if (isTest) {
			apply(plugin = "kotlin-allopen")
			allOpen {
				annotation("com.babestudios.base.annotation.Mockable")
			}
		}
	}

	//Added due to issue with AGP 7.1.1 and Hilt. See https://issuetracker.google.com/issues/219572935
	hilt {
		enableAggregatingTask = false
	}
}

dependencies {
	implementation(project(":domain"))
	implementation(project(":data"))
	implementation(project(":feature-charges"))
	implementation(project(":feature-companies"))
	implementation(project(":feature-filings"))
	implementation(project(":feature-insolvencies"))
	implementation(project(":feature-officers"))
	implementation(project(":feature-persons"))
	implementation(project(":navigation"))

	implementation(Libs.baBeStudiosBase)
	implementation(Libs.AndroidX.appcompat)
	implementation(Libs.AndroidX.Navigation.uiKtx)
	implementation(Libs.Google.Dagger.core)
	implementation(Libs.Google.Dagger.Hilt.android)
	implementation(Libs.Google.gson)
	implementation(platform(Libs.Google.Firebase.bom))
	implementation(Libs.Google.Firebase.crashlytics)
	implementation(Libs.SquareUp.Retrofit2.retrofit)

	kapt(Libs.AndroidX.Hilt.compiler)
	kapt(Libs.Google.Dagger.compiler)
	kapt(Libs.Google.Dagger.Hilt.compiler)

	androidTestImplementation(Libs.Google.gson)
	androidTestImplementation(Libs.SquareUp.Retrofit2.retrofit)
	androidTestImplementation(Libs.SquareUp.Retrofit2.rxJava2Adapter)
	androidTestImplementation(Libs.Google.Firebase.analytics)
	androidTestImplementation(Libs.SquareUp.OkHttp3.loggingInterceptor)
	androidTestImplementation(Libs.Test.MockK.android)
	androidTestImplementation(Libs.Test.barista)
	androidTestImplementation(Libs.Google.Dagger.Hilt.androidTesting)
	androidTestImplementation(Libs.AndroidX.Test.Espresso.core)
	//RecyclerView, ViewPager, NavigationViewActions: unused for now
	//androidTestImplementation (Libs.AndroidX.Test.Espresso.contrib)
	androidTestImplementation(Libs.AndroidX.Test.Ext.jUnit)
	androidTestImplementation(Libs.AndroidX.Test.rules)
	androidTestImplementation(Libs.AndroidX.Test.runner)
	androidTestImplementation(Libs.Test.barista)

	kaptAndroidTest(Libs.Google.Dagger.compiler)
	kaptAndroidTest(Libs.Google.Dagger.Hilt.compiler)
	kaptAndroidTest(Libs.Google.Dagger.Hilt.androidTestingCompiler)

}
