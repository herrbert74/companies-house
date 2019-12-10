import com.babestudios.companyinfouk.buildsrc.Libs

plugins {
	id("jacoco") version "0.7.1.201405082137" apply false
	id("io.fabric")
}
	/*jacoco {

	}*/

	android {
		defaultConfig {
			applicationId = "com.babestudios.companyinfouk"
			versionCode = 9
			versionName = "1.2"
			vectorDrawables.useSupportLibrary = true
			testInstrumentationRunner = "com.babestudios.companyinfouk.CompaniesHouseAndroidJUnitRunner"
		}
		signingConfigs {

			getByName("release") {
				storeFile = file("CH_RELEASE_STORE_FILE")
				storePassword = "CH_RELEASE_STORE_PASSWORD"
				keyAlias = "CH_RELEASE_KEY_ALIAS"
				keyPassword = "CH_RELEASE_KEY_PASSWORD"
			}
		}
		buildTypes {
			getByName("release") {
				//testCoverageEnabled true
				isDebuggable = false
				isMinifyEnabled = true
				signingConfig = signingConfigs.getByName("release")
				//proguardFiles = getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
			}
			getByName("debug") {
				isTestCoverageEnabled = true
				isMinifyEnabled = false
				//proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
			}
		}
		testOptions {
			//unitTests.returnDefaultValues = true
			//unitTests { includeAndroidResources = true }
		}

		androidExtensions {
			isExperimental = true
		}

		applicationVariants.all { variant ->
			val isTest :Boolean = gradle.startParameter.taskNames.find {it.contains("test") || it.contains("Test")} != null
			if (isTest) {
				/*apply("kotlin-allopen")
				allOpen {
					annotation('com.babestudios.base.annotation.Mockable')
				}*/
			}
		}
	}

	dependencies {
		implementation (project(":feature-charges"))
		implementation (project(":feature-companies"))
		implementation (project(":feature-filings"))
		implementation (project(":feature-insolvencies"))
		implementation (project(":feature-officers"))
		implementation (project(":feature-persons"))
		androidTestImplementation (project(":data"))

		implementation (Libs.Google.crashlytics)
				implementation (Libs.AndroidX.appcompat)
				implementation (Libs.Stetho.core)
				//debugImplementation Libs.debugDb
				implementation (Libs.AndroidX.Navigation.ktx)
				implementation (Libs.AndroidX.coreKtx)
				implementation (Libs.Google.Dagger.dagger)

				androidTestImplementation (Libs.Google.gson)
				androidTestImplementation (Libs.SquareUp.Retrofit2.retrofit)
				androidTestImplementation (Libs.SquareUp.Retrofit2.rxJava2Adapter)
				androidTestImplementation (Libs.Google.Firebase.analytics)
				androidTestImplementation (Libs.SquareUp.OkHttp3.loggingInterceptor)
				kapt (Libs.Google.Dagger.compiler)
				kaptAndroidTest (Libs.Google.Dagger.compiler)

				androidTestImplementation (Libs.Test.mockKAndroidTest)
				androidTestImplementation (Libs.Test.conditionWatcher)
				androidTestImplementation (Libs.AndroidX.Test.Espresso.core)
				//RecyclerView, ViewPager, NavigationViewActions: unused for now
				//androidTestImplementation (Libs.AndroidX.Test.Espresso.contrib)
				androidTestImplementation (Libs.AndroidX.Test.Ext.jUnit)
				androidTestImplementation (Libs.AndroidX.Test.rules)
				androidTestImplementation (Libs.AndroidX.Test.runner)
	}

	apply ("com.google.gms.google-services")
}
