import com.babestudios.companyinfouk.buildsrc.Libs

plugins {
	id("com.babestudios.companyinfouk.plugins.android")
	id("com.google.firebase.crashlytics")
	id("com.google.gms.google-services")
	id("org.jetbrains.kotlin.plugin.allopen")
	id("dagger.hilt.android.plugin")
	id("kotlin-parcelize")
}

@Suppress("UnstableApiUsage")
android {

	namespace = "com.babestudios.companyinfouk"

	buildFeatures.compose = true

	composeOptions {
		kotlinCompilerExtensionVersion = "1.4.2"
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
	implementation(project(":common"))
	implementation(project(":domain"))
	implementation(project(":data"))
	implementation(project(":feature-charges"))
	implementation(project(":feature-companies"))
	implementation(project(":feature-filings"))
	implementation(project(":feature-insolvencies"))
	implementation(project(":feature-officers"))
	implementation(project(":feature-persons"))

	implementation(Libs.baBeStudiosBase) {
		exclude("androidx.navigation","navigation-fragment-ktx")
		exclude("androidx.navigation","navigation-ui-ktx")
	}
	implementation(Libs.AndroidX.appcompat)
	implementation(Libs.AndroidX.Compose.animation)
	implementation(Libs.AndroidX.Activity.compose)
	implementation(Libs.Decompose.core)
	implementation(Libs.Decompose.extensionsJetBrains)
	implementation(Libs.Google.Dagger.core)
	implementation(Libs.Google.Dagger.Hilt.android)
	implementation(platform(Libs.Google.Firebase.bom))
	implementation(Libs.Google.Firebase.crashlytics)
	implementation(Libs.AndroidX.Compose.runtime) //To make buildFeatures.compose = true happy ?!

	kapt(Libs.Google.Dagger.compiler)

	androidTestImplementation(Libs.Google.gson)
	androidTestImplementation(Libs.SquareUp.Retrofit2.retrofit)
	androidTestImplementation(Libs.Google.Firebase.analytics)
	androidTestImplementation(Libs.SquareUp.OkHttp3.loggingInterceptor)
	androidTestImplementation(Libs.Google.Dagger.Hilt.androidTesting)
	implementation(platform(Libs.AndroidX.Compose.bom))
	androidTestImplementation(platform(Libs.AndroidX.Compose.bom))
	androidTestImplementation(Libs.AndroidX.Compose.Ui.test)
	debugImplementation(Libs.AndroidX.Compose.Ui.testManifest)
	androidTestImplementation(Libs.AndroidX.Test.Espresso.core)
	androidTestImplementation(Libs.AndroidX.Test.Ext.jUnit)
	androidTestImplementation(Libs.AndroidX.Test.runner)
	androidTestImplementation(Libs.Decompose.core)
	androidTestImplementation(Libs.KotlinResult.result)
	androidTestImplementation(Libs.Test.jUnit)
	androidTestImplementation(Libs.Test.MockK.core)

	kaptAndroidTest(Libs.Google.Dagger.Hilt.androidTestingCompiler)

}
