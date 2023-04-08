plugins {
	id("com.babestudios.companyinfouk.plugins.android")
	id("com.google.firebase.crashlytics")
	id("com.google.gms.google-services")
	id("org.jetbrains.kotlin.plugin.allopen")
	id("kotlin-parcelize")
}

@Suppress("UnstableApiUsage")
android {

	namespace = "com.babestudios.companyinfouk"

	buildFeatures.compose = true
	buildFeatures.buildConfig = true

	composeOptions {
		kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
	}

	defaultConfig {
		applicationId = "com.babestudios.companyinfouk"
		versionCode = libs.versions.versionCode.get().toInt()
		versionName = libs.versions.versionName.toString()
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

	implementation(libs.baBeStudiosBase) {
		exclude("androidx.navigation","navigation-fragment-ktx")
		exclude("androidx.navigation","navigation-ui-ktx")
	}
	implementation(libs.androidx.appcompat)
	implementation(libs.androidx.compose.animation)
	implementation(libs.androidx.activity.compose)
	implementation(libs.decompose.core)
	implementation(libs.decompose.extensionsJetBrains)
	implementation(platform(libs.google.firebase.bom))
	implementation(libs.google.firebase.crashlytics)
	implementation(libs.androidx.compose.runtime) //To make buildFeatures.compose = true happy ?!

	debugImplementation(platform(libs.androidx.compose.bom))
	debugImplementation(libs.androidx.compose.ui.testManifest)

	androidTestImplementation(libs.google.gson)
	androidTestImplementation(libs.squareUp.retrofit2.retrofit)
	androidTestImplementation(platform(libs.google.firebase.bom))
	androidTestImplementation(libs.google.firebase.analytics)
	androidTestImplementation(libs.squareUp.okhttp3.loggingInterceptor)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.compose.ui.test)
	androidTestImplementation(libs.androidx.test.espresso.core)
	androidTestImplementation(libs.androidx.test.ext.jUnitKtx)
	androidTestImplementation(libs.androidx.test.runner)
	androidTestImplementation(libs.decompose.core)
	androidTestImplementation(libs.kotlinResult.result)
	androidTestImplementation(libs.test.jUnit)
	androidTestImplementation(libs.test.mockk.androidTest)

}
