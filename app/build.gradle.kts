plugins {
//	id("com.babestudios.companyinfouk.plugins.android")
	alias(libs.plugins.android.application)
	id("org.jetbrains.kotlin.android")
	//alias(libs.plugins.kotlin.multiplatform)
	id("com.google.firebase.crashlytics")
//	id("com.google.gms.google-services")
//	id("org.jetbrains.kotlin.plugin.allopen")
	id("kotlin-parcelize")
}

android {

	namespace = "com.babestudios.companyinfouk"

	buildFeatures.buildConfig = true

	defaultConfig {
		applicationId = "com.babestudios.companyinfouk"
		versionCode = libs.versions.versionCode.get().toInt()
		versionName = libs.versions.versionName.toString()
		minSdk = libs.versions.minSdkVersion.get().toInt()
		compileSdk = libs.versions.compileSdkVersion.get().toInt()
		targetSdk = libs.versions.targetSdkVersion.get().toInt()
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

	packaging {
		resources.excludes.add("META-INF/LICENSE.md")
		resources.excludes.add("META-INF/LICENSE-notice.md")
	}

}

kotlin {
	jvmToolchain(21)
}

dependencies {

	implementation(project(":shared"))

	runtimeOnly(project(":feature-main"))

	implementation(platform(libs.google.firebase.bom))
	implementation(libs.google.firebase.crashlytics)
	implementation(libs.koin.core)
	implementation(libs.koin.android)

}
