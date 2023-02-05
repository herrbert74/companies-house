import com.babestudios.companyinfouk.buildsrc.Libs

plugins {
	id("com.babestudios.companyinfouk.plugins.android")
	id("jacoco")
	id("com.google.firebase.crashlytics")
	id("com.google.gms.google-services")
	id("org.jetbrains.kotlin.plugin.allopen")
	id("com.google.devtools.ksp") version "1.8.0-1.0.9"
	id("dagger.hilt.android.plugin")
}

@Suppress("UnstableApiUsage")
android {

	buildFeatures.compose = true

	composeOptions {
		kotlinCompilerExtensionVersion = "1.4.0"
	}

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
			isDebuggable = false
			isMinifyEnabled = true
			signingConfig = signingConfigs.getByName("release")
			proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
		}
		getByName("debug") {
			isMinifyEnabled = false
		}
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
	implementation(Libs.AndroidX.Compose.runtime) //To make buildFeatures.compose = true happy ?!

	kapt(Libs.AndroidX.Hilt.compiler)
	kapt(Libs.Google.Dagger.compiler)
	kapt(Libs.Google.Dagger.Hilt.compiler)

	androidTestImplementation(Libs.Google.gson)
	androidTestImplementation(Libs.SquareUp.Retrofit2.retrofit)
	androidTestImplementation(Libs.Google.Firebase.analytics)
	androidTestImplementation(Libs.SquareUp.OkHttp3.loggingInterceptor)
	androidTestImplementation(Libs.Test.MockK.android)
	androidTestImplementation(Libs.Test.barista)
	androidTestImplementation(Libs.Google.Dagger.Hilt.androidTesting)
	implementation(platform(Libs.AndroidX.Compose.bom))
	androidTestImplementation(platform(Libs.AndroidX.Compose.bom))
	androidTestImplementation(Libs.AndroidX.Compose.Ui.test)
	debugImplementation(Libs.AndroidX.Compose.Ui.testManifest)
	androidTestImplementation(Libs.AndroidX.Test.Espresso.core)
	//RecyclerView, ViewPager, NavigationViewActions: unused for now
	//androidTestImplementation (Libs.AndroidX.Test.Espresso.contrib)
	androidTestImplementation(Libs.AndroidX.Test.Ext.jUnit)
	androidTestImplementation(Libs.AndroidX.Test.rules)
	androidTestImplementation(Libs.AndroidX.Test.runner)
	androidTestImplementation(Libs.Decompose.core)
	androidTestImplementation(Libs.Test.barista)
	androidTestImplementation(Libs.KotlinResult.result)

	kaptAndroidTest(Libs.Google.Dagger.compiler)
	kaptAndroidTest(Libs.Google.Dagger.Hilt.compiler)
	kaptAndroidTest(Libs.Google.Dagger.Hilt.androidTestingCompiler)

}
