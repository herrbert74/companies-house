package com.babestudios.companyinfouk.buildsrc

object Versions {
	const val minSdkVersion = 21
	const val compileSdkVersion = 33
	const val targetSdkVersion = 33
	const val gradlePlugin = "7.1.3"
	const val kotlin = "1.8.0"
	const val coroutines = "1.6.4"
	const val dagger = "2.44.2"
}

//Sadly this doesn't work in root gradle file, probably chicken and egg problem
@Suppress("unused", "SpellCheckingInspection")
object Plugins {
	const val gradle = "com.android.tools.build:gradle:${Versions.gradlePlugin}"
	const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
	const val kotlinAllOpen = "org.jetbrains.kotlin:kotlin-allopen:${Versions.kotlin}"
	const val googleServices = "com.google.gms:google-services:4.3.10"
	const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics-gradle:2.8.1"
	const val firebaseAppDistribution = "com.google.firebase:firebase-appdistribution-gradle:3.0.0"
	const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Versions.dagger}"
	const val navigationSafeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:2.5.2"
	const val detekt = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.21.0"
}

@Suppress("unused", "SpellCheckingInspection")
object Libs {
	const val baBeStudiosBase = "org.bitbucket.herrbert74:babestudiosbase:2.0.3"

	object AndroidX {
		const val annotation = "androidx.annotation:annotation:1.3.0"
		const val appcompat = "androidx.appcompat:appcompat:1.5.1"
		const val appCompatResources = "androidx.appcompat:appcompat-resources:1.4.2"
		const val browser = "androidx.browser:browser:1.3.0"
		const val cardView = "androidx.cardview:cardview:1.0.0"
		const val collection = "androidx.collection:collection:1.2.0"
		const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"
		const val constraintLayoutCompose = "androidx.constraintlayout:constraintlayout-compose:1.0.1"
		const val coreKtx = "androidx.core:core-ktx:1.8.0"
		const val datastoreCore = "androidx.datastore:datastore-preferences-core:1.0.0"
		const val datastoreRx = "androidx.datastore:datastore-preferences-rxjava2:1.0.0"
		const val fragmentKtx = "androidx.fragment:fragment-ktx:1.6.0"
		const val media = "androidx.media:media:1.4.3"
		const val mediaRouter = "androidx.mediarouter:mediarouter:1.2.4"
		const val preferenceKtx = "androidx.preference:preference-ktx:1.1.0"
		const val recyclerView = "androidx.recyclerview:recyclerview:1.2.1"
		const val splash = "androidx.core:core-splashscreen:1.0.0-alpha01"
		const val startup = "androidx.startup:startup-runtime:1.1.1"
		const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
		const val viewPager2 = "androidx.viewpager2:viewpager2:1.0.0"
		const val window = "androidx.window:window:1.0.0-alpha06"

		object Activity {
			private const val version = "1.6.1"
			const val activityKtx = "androidx.activity:activity-ktx:$version"
			const val compose = "androidx.activity:activity-compose:$version"
		}

		object Compose {
			private const val bomVersion = "2023.01.00"
			private const val material3Version = "1.1.0-alpha05" //This had search bar
			private const val composeUiVersion = "1.4.0-alpha05" //This has a fix for a crash

			//https://developer.android.com/jetpack/compose/setup#bom-version-mapping
			//https://developer.android.com/jetpack/compose/bom/bom-mapping
			const val bom = "androidx.compose:compose-bom:$bomVersion"

			object Ui {

				//Compile: ui-geometry, ui-graphics, ui-text and ui-unit; runtime: ui-util
				const val primitives = "androidx.compose.ui:ui:$composeUiVersion"

				//Compile: ui, runtime, ui-tooling-preview, ui-tooling-data
				const val tooling = "androidx.compose.ui:ui-tooling"

				//Compile: ui-test
				const val test = "androidx.compose.ui:ui-test-junit4"
				const val testManifest = "androidx.compose.ui:ui-test-manifest" //For adding Activities

			}

			const val runtime = "androidx.compose.runtime:runtime"
			const val animation = "androidx.compose.animation:animation-core"

			//Compile: ui, runtime, animation, material-icons-core, material-ripple
			const val material = "androidx.compose.material:material"
			const val material3 = "androidx.compose.material3:material3:$material3Version"

			const val foundationLayout = "androidx.compose.foundation:foundation-layout"

		}

		object Hilt {
			private const val version = "1.0.0"
			const val compiler = "androidx.hilt:hilt-compiler:$version"
			const val work = "androidx.hilt:hilt-work:$version"
			const val navigation = "androidx.hilt:hilt-navigation:$version"
			const val navigationFragment = "androidx.hilt:hilt-navigation-fragment:$version"
		}

		object Lifecycle {
			private const val version = "2.5.0"
			const val common = "androidx.lifecycle:lifecycle-common:$version"
			const val runtime = "androidx.lifecycle:lifecycle-runtime:$version"
			const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
			const val process = "androidx.lifecycle:lifecycle-process:$version"

			object ViewModel {
				const val ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
				const val savedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:$version"
			}
		}

		object Navigation {
			private const val version = "2.5.2"
			const val common = "androidx.navigation:navigation-common:$version"
			const val runtime = "androidx.navigation:navigation-runtime:$version"
			const val uiKtx = "androidx.navigation:navigation-ui-ktx:$version"
			const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
			const val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
		}

		object WorkManager {
			private const val version = "2.7.0"
			const val ktx = "androidx.work:work-runtime-ktx:$version"
			const val rxJava = "androidx.work:work-rxjava2:$version"
			const val testing = "androidx.work:work-testing:$version"
		}

		object Room {
			private const val version = "2.3.0"
			const val common = "androidx.room:room-common:$version"
			const val runtime = "androidx.room:room-runtime:$version"
			const val compiler = "androidx.room:room-compiler:$version"
			const val rxJava = "androidx.room:room-rxjava2:$version"
		}

		object Test {
			private const val version = "1.4.0"

			//Provided by androidx.test.ext:junit
			//const val core = "androidx.test:core:$version"
			const val coreKtx = "androidx.test:core-ktx:$version"
			const val runner = "androidx.test:runner:$version"
			const val rules = "androidx.test:rules:$version"
			const val orchestrator = "androidx.test:orchestrator:$version"
			const val uiAutomator = "androidx.test.uiautomator:uiautomator:2.2.0"

			object Espresso {
				private const val version = "3.4.0"
				const val core = "androidx.test.espresso:espresso-core:$version"
				const val contrib = "androidx.test.espresso:espresso-contrib:$version"
				const val intents = "androidx.test.espresso:espresso-contrib:$version"
			}

			object Ext {
				private const val version = "1.1.3"
				const val jUnit = "androidx.test.ext:junit:$version"
				const val jUnitKtx = "androidx.test.ext:junit-ktx:$version"
			}
		}

		object Arch {
			object Core {
				const val testing = "androidx.arch.core:core-testing:2.1.0"
			}
		}
	}

	object Google {
		const val material = "com.google.android.material:material:1.5.0"
		const val gson = "com.google.code.gson:gson:2.9.0"

		object Play {
			const val core = "com.google.android.play:core:1.10.0"
			const val coreKtx = "com.google.android.play:core-ktx:1.8.1"
		}

		object PlayServices {
			const val ads = "com.google.android.gms:play-services-ads:20.5.0"
			const val analytics = "com.google.android.gms:play-services-analytics:17.0.1"
			const val auth = "com.google.android.gms:play-services-auth:19.2.0"
			const val location = "com.google.android.gms:play-services-location:18.0.0"
			const val maps = "com.google.android.gms:play-services-maps:18.1.0"
			const val mapsCompose = "com.google.maps.android:maps-compose:2.8.0"
		}

		object Dagger {
			const val core = "com.google.dagger:dagger:${Versions.dagger}"
			const val compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"

			object Hilt {
				const val core = "com.google.dagger:hilt-core:${Versions.dagger}"
				const val android = "com.google.dagger:hilt-android:${Versions.dagger}"
				const val compiler = "com.google.dagger:hilt-compiler:${Versions.dagger}"
				const val androidTesting = "com.google.dagger:hilt-android-testing:${Versions.dagger}"
				const val androidTestingCompiler = "com.google.dagger:hilt-android-compiler:${Versions.dagger}"
			}
		}

		object Firebase {
			const val bom = "com.google.firebase:firebase-bom:29.3.1"
			const val analytics = "com.google.firebase:firebase-analytics-ktx"
			const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
			const val messaging = "com.google.firebase:firebase-messaging-ktx"
			const val database = "com.google.firebase:firebase-database-ktx"
		}
	}

	object Kotlin {

		const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
		const val test = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"
		const val jUnit = "org.jetbrains.kotlin:kotlin-test-jUnit:${Versions.kotlin}"
		const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"

		object Coroutines {
			const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
			const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
			const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
			const val rx2 = "org.jetbrains.kotlinx:kotlinx-coroutines-rx2:${Versions.coroutines}"
			const val playServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.coroutines}"
		}

	}

	object SquareUp {

		const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.8.1"
		const val javaPoet = "com.squareup:javapoet:1.13.0" //Needed for AGP: https://github.com/google/dagger/issues/3068

		object Moshi {
			private const val version = "1.8.0"
			const val core = "com.squareup.moshi:moshi:$version"
			const val kotlin = "com.squareup.moshi:moshi-kotlin:$version"
		}

		object OkHttp3 {
			private const val version = "4.10.0"
			const val okHttp = "com.squareup.okhttp3:okhttp:$version"
			const val mockWebServer = "com.squareup.okhttp3:mockwebserver:$version"
			const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
		}

		object Retrofit2 {
			private const val version = "2.9.0"
			const val retrofit = "com.squareup.retrofit2:retrofit:$version"
			const val retrofitMock = "com.squareup.retrofit2:retrofit-mock:$version"
			const val converterGson = "com.squareup.retrofit2:converter-gson:$version"
		}

		object SqlDelight {
			private const val version = "1.4.4"
			const val plugin = "com.squareup.sqldelight:gradle-plugin:$version"
			const val driver = "com.squareup.sqldelight:android-driver:$version"
		}

	}

	object JakeWharton {
		const val timber = "com.jakewharton.timber:timber:5.0.1"
	}

	object Room {
		private const val version = "2.1.0"
		const val runtime = "android.arch.persistence.room:runtime:$version"
		const val compiler = "android.arch.persistence.room:compiler:$version"
	}

	object Javax {
		const val annotations = "javax.annotation:javax.annotation-api:1.3.2"
		const val inject = "javax.inject:javax.inject:1"
	}

	object Chucker {
		private const val version = "3.5.2"
		const val library = "com.github.ChuckerTeam.Chucker:library:$version"
		const val noop = "com.github.ChuckerTeam.Chucker:library-no-op:$version"
	}

	object MviKotlin {
		private const val version = "3.1.0"
		const val core = "com.arkivanov.mvikotlin:mvikotlin:$version"
		const val main = "com.arkivanov.mvikotlin:mvikotlin-main:$version"
		const val rx = "com.arkivanov.mvikotlin:rx:$version"
		const val rxInternal = "com.arkivanov.mvikotlin:rx-internal:$version"
		const val utilsInternal = "com.arkivanov.mvikotlin:utils-internal:$version"
		const val coroutines = "com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:$version"
		const val logging = "com.arkivanov.mvikotlin:mvikotlin-logging:$version"
	}

	object Decompose {
		private const val VERSION = "1.0.0"
		const val core = "com.arkivanov.decompose:decompose:$VERSION"
		const val extensionsJetpack = "com.arkivanov.decompose:extensions-compose-jetpack:$VERSION"
		const val extensionsJetBrains = "com.arkivanov.decompose:extensions-compose-jetbrains:$VERSION"
	}

	object KotlinResult {
		private const val version = "1.1.16"
		const val result = "com.michael-bull.kotlin-result:kotlin-result:$version"
		const val coroutines = "com.michael-bull.kotlin-result:kotlin-result-coroutines:$version"
	}

	object Test {
		const val jUnit = "junit:junit:4.13.2"
		const val assertJ = "org.assertj:assertj-core:3.22.0"
		const val robolectric = "org.robolectric:robolectric:4.8.1"
		const val barista = "com.adevinta.android:barista:4.2.0"

		object JUnit5 {
			private const val version = "5.8.2"
			const val jupiterApi = "org.junit.jupiter:junit-jupiter-api:$version"
			const val jupiterEngine = "org.junit.jupiter:junit-jupiter-engine:$version"
			const val jupiterParams = "org.junit.jupiter:junit-jupiter-params:$version"
			const val vintageEngine = "org.junit.jupiter:junit-vintage-engine:$version"
		}

		object MockK {
			private const val version = "1.12.5"
			const val core = "io.mockk:mockk:$version"
			const val android = "io.mockk:mockk-android:$version"
		}

		object Kotest {
			private const val version = "5.3.1"
			const val runner = "io.kotest:kotest-runner-junit5:$version"
			const val assertions = "io.kotest:kotest-assertions-core:$version"
			const val property = "io.kotest:kotest-property:$version"
		}
	}

	object Detekt {
		private const val version = "1.21.0"
		const val api = "io.gitlab.arturbosch.detekt:detekt-api:$version"
		const val cli = "io.gitlab.arturbosch.detekt:detekt-cli:$version"
		const val test = "io.gitlab.arturbosch.detekt:detekt-test:$version"
	}

	object Views {

		object FlowBinding {
			private const val version = "1.2.0"
			const val android = "io.github.reactivecircus.flowbinding:flowbinding-android:$version"
			const val appcompat = "io.github.reactivecircus.flowbinding:flowbinding-appcompat:$version"
			const val core = "io.github.reactivecircus.flowbinding:flowbinding-core:$version"
			const val drawerLayout = "io.github.reactivecircus.flowbinding:flowbinding-drawerLayout:$version"
			const val lifecycle = "io.github.reactivecircus.flowbinding:flowbinding-lifecycle:$version"
			const val navigation = "io.github.reactivecircus.flowbinding:flowbinding-navigation:$version"
			const val preference = "io.github.reactivecircus.flowbinding:flowbinding-preference:$version"
			const val recyclerView = "io.github.reactivecircus.flowbinding:flowbinding-recyclerview:$version"
			const val swipeRefreshLayout =
				"io.github.reactivecircus.flowbinding:flowbinding-swiperefreshlayout:$version"
			const val viewPager2 = "io.github.reactivecircus.flowbinding:flowbinding-viewpager2:$version"
			const val material = "io.github.reactivecircus.flowbinding:flowbinding-material:$version"
		}

		const val collapsingToolbar = "me.onebone:toolbar-compose:2.3.5"
	}
}
