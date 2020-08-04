import com.babestudios.companyinfouk.buildsrc.Libs

plugins {
	id("com.babestudios.companyinfouk.plugins.android")
	id("jacoco")
	id("com.google.firebase.crashlytics")
	id("com.google.gms.google-services")
	id("org.jetbrains.kotlin.plugin.allopen")
}

android {
	defaultConfig {
		applicationId = "com.babestudios.companyinfouk"
		versionCode = 9
		versionName = "1.2"
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

	androidExtensions {
		isExperimental = true
	}

	applicationVariants.all {
		val isTest: Boolean = gradle.startParameter.taskNames.find { it.contains("test") || it.contains("Test") } != null
		if (isTest) {
			apply(plugin = "kotlin-allopen")
			allOpen {
				annotation("com.babestudios.base.annotation.Mockable")
			}
		}
	}

	tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
		kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
	}
}

dependencies {
	implementation(project(":feature-charges"))
	implementation(project(":feature-companies"))
	implementation(project(":feature-filings"))
	implementation(project(":feature-insolvencies"))
	implementation(project(":feature-officers"))
	implementation(project(":feature-persons"))
	androidTestImplementation(project(":data"))

	implementation(Libs.Google.Firebase.crashlytics)
	implementation(Libs.AndroidX.appcompat)
	debugImplementation(Libs.Facebook.Flipper.debug)
	releaseImplementation(Libs.Facebook.Flipper.release)
	implementation(Libs.Facebook.soloader)
	implementation(Libs.AndroidX.Navigation.ktx)
	implementation(Libs.AndroidX.coreKtx)
	implementation(Libs.Google.Dagger.dagger)

	androidTestImplementation(Libs.Google.gson)
	androidTestImplementation(Libs.SquareUp.Retrofit2.retrofit)
	androidTestImplementation(Libs.SquareUp.Retrofit2.rxJava2Adapter)
	androidTestImplementation(Libs.Google.Firebase.analytics)
	androidTestImplementation(Libs.SquareUp.OkHttp3.loggingInterceptor)
	kapt(Libs.Google.Dagger.compiler)
	kaptAndroidTest(Libs.Google.Dagger.compiler)

	androidTestImplementation(Libs.Test.mockKAndroidTest)
	androidTestImplementation(Libs.Test.conditionWatcher)
	androidTestImplementation(Libs.AndroidX.Test.Espresso.core)
	//RecyclerView, ViewPager, NavigationViewActions: unused for now
	//androidTestImplementation (Libs.AndroidX.Test.Espresso.contrib)
	androidTestImplementation(Libs.AndroidX.Test.Ext.jUnit)
	androidTestImplementation(Libs.AndroidX.Test.rules)
	androidTestImplementation(Libs.AndroidX.Test.runner)
}
