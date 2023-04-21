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

	packagingOptions {
		resources.excludes.add("META-INF/LICENSE.md")
		resources.excludes.add("META-INF/LICENSE-notice.md")
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

	implementation(libs.androidx.activity.activity) //Transitive
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.compose.runtime) //To make buildFeatures.compose = true happy ?!
	implementation(libs.androidx.compose.animation.core) //Transitive
	implementation(libs.androidx.compose.ui) //Transitive
	implementation(libs.androidx.lifecycle.common) //Transitive
	implementation(libs.androidx.lifecycle.viewmodel) //Transitive
	implementation(libs.androidx.savedState) //Transitive
	implementation(libs.androidx.core) //Transitive
	implementation(libs.decompose.core)
	implementation(libs.decompose.extensionsJetBrains)
	implementation(platform(libs.google.firebase.bom))
	implementation(libs.google.firebase.crashlytics)
	implementation(libs.koin.core)
	implementation(libs.koin.android)
	implementation(libs.kotlin.parcelize.runtime) //Transitive
	implementation(libs.kotlinx.coroutines.core) //Transitive

	debugRuntimeOnly(platform(libs.androidx.compose.bom))
	//Needed for createComposeRule, NOT ONLY for createAndroidComposeRule, as in the docs
	debugRuntimeOnly(libs.androidx.compose.ui.testManifest)

	androidTestImplementation(libs.baBeStudios.base.kotlin)
	androidTestImplementation(libs.google.gson)
	androidTestImplementation(platform(libs.google.firebase.bom))
	androidTestImplementation(libs.google.firebase.analytics)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.androidx.compose.ui.test.junit4)
	androidTestImplementation(libs.androidx.test.espresso.core)
	//androidTestImplementation(libs.androidx.test.ext.jUnitKtx) //For ActivityScenario
	androidTestImplementation(libs.androidx.test.runner)
	androidTestImplementation(libs.decompose.core)
	androidTestImplementation(libs.kotlinResult.result)
	androidTestImplementation(libs.test.jUnit)
	androidTestImplementation(libs.bundles.mockk.android)
	androidTestImplementation(libs.androidx.compose.ui.test) //Transitive
	androidTestImplementation(libs.androidx.compose.ui.text) //Transitive
	androidTestImplementation(libs.androidx.test.ext.jUnit) //Transitive from androidx.compose.ui:ui-test-junit4
	androidTestImplementation(libs.test.hamcrest) //Transitive

}
