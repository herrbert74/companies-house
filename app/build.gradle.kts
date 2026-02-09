plugins {
	alias(libs.plugins.androidApplication)
	alias(libs.plugins.crashlytics)
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
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
	jvmToolchain {
		languageVersion.set(JavaLanguageVersion.of(libs.versions.jdk.get()))
	}
}

dependencies {

	implementation(project(":shared"))

	implementation(project(":feature-main"))

	implementation(platform(libs.firebaseBom))
	implementation(libs.firebaseCrashlytics)
	implementation(libs.koin.core)
	implementation(libs.koin.android)

}
