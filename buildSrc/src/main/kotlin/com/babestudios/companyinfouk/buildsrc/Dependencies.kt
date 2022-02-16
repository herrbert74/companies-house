package com.babestudios.companyinfouk.buildsrc

import org.gradle.api.artifacts.dsl.DependencyHandler

//Sadly this doesn't work in root gradle file, probably chicken and egg problem
@Suppress("unused", "SpellCheckingInspection")
object Plugins {
	private const val kotlinVersion = "1.6.10"
	const val androidTools = "com.android.tools.build:gradle:4.2.0-alpha08"
	const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
	const val kotlinAllOpen = "org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion"
	const val googleServices = "com.google.gms:google-services:4.3.3"
	//const val fabric = "io.fabric.tools:gradle:1.31.2"
	const val detekt = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.19.0"
}

@Suppress("unused", "SpellCheckingInspection")
object Libs {
	const val baBeStudiosBase = "org.bitbucket.herrbert74:babestudiosbase:1.2.15"

	object AndroidX {
		const val appcompat = "androidx.appcompat:appcompat:1.2.0"
		const val activityKtx = "androidx.activity:activity-ktx:1.1.0"
		const val fragmentKtx = "androidx.fragment:fragment-ktx:1.2.5"
		const val cardView = "androidx.cardview:cardview:1.0.0"
		const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
		const val androidxAnnotations = "androidx.annotation:annotation:1.1.0"
		const val coreKtx = "androidx.core:core-ktx:1.3.2"
		const val recyclerView = "androidx.recyclerview:recyclerview:1.0.0"
		const val viewPager2 = "androidx.viewpager2:viewpager2:1.0.0-beta04"
		const val preferenceKtx = "androidx.preference:preference-ktx:1.1.1"


		object Lifecycle {
			private const val version = "2.4.0"
			const val common = "androidx.lifecycle:lifecycle-common:$version"
			const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
			const val commonJava8 = "androidx.lifecycle:lifecycle-common-java8:$version"

			object ViewModel {
				const val ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
				const val savedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:$version"
			}
		}

		object Navigation {
			private const val version = "2.4.0"
			const val ktx = "androidx.navigation:navigation-ui-ktx:$version"
			const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
		}

		object WorkManager {
			private const val version = "2.3.0-alpha01"
			const val ktx = "androidx.work:work-runtime-ktx:$version"
		}

		object Test {
			private const val version = "1.3.0"
			const val core = "androidx.test:core:$version"
			const val runner = "androidx.test:runner:$version"
			const val rules = "androidx.test:rules:$version"

			object Espresso {
				private const val version = "3.3.0"
				const val core = "androidx.test.espresso:espresso-core:$version"
				const val contrib = "androidx.test.espresso:espresso-contrib:$version"
			}

			object Ext {
				const val jUnit = "androidx.test.ext:junit:1.1.2"
			}
		}
	}

	object Google {
		const val material = "com.google.android.material:material:1.3.0"
		const val gson = "com.google.code.gson:gson:2.8.6"


		object PlayServices {
			private const val version = "17.0.0"
			const val gcm = "com.google.android.gms:play-services-gcm:$version"
			const val analytics = "com.google.android.gms:play-services-analytics:$version"
			const val location = "com.google.android.gms:play-services-location:$version"
			const val maps = "com.google.android.gms:play-services-maps:$version"
		}

		object Dagger {
			private const val version = "2.40.5"
			const val dagger = "com.google.dagger:dagger:$version"
			const val compiler = "com.google.dagger:dagger-compiler:$version"
		}

		object Firebase {
			const val bom = "com.google.firebase:firebase-bom:29.1.0"
			const val analytics = "com.google.firebase:firebase-analytics-ktx"
			const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
			const val messaging = "com.google.firebase:firebase-messaging-ktx"
			const val database = "com.google.firebase:firebase-database-ktx"
		}
	}

	object SquareUp {
		object OkHttp3 {
			private const val version = "4.9.1"
			const val okHttp3 = "com.squareup.okhttp3:okhttp:$version"
			const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
		}

		object Retrofit2 {
			private const val version = "2.9.0"
			const val retrofit = "com.squareup.retrofit2:retrofit:$version"
			const val retrofitMock = "com.squareup.retrofit2:retrofit-mock:$version"
			const val converterGson = "com.squareup.retrofit2:converter-gson:$version"
			const val rxJava2Adapter = "com.squareup.retrofit2:adapter-rxjava2:$version"
		}
	}

	object RxJava2 {
		const val rxJava = "io.reactivex.rxjava2:rxjava:2.2.19"
		const val rxAndroid = "io.reactivex.rxjava2:rxandroid:2.1.1"
		const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:2.4.0"

		fun DependencyHandler.implementRx() {
			add("implementation", rxJava)
			add("implementation", rxAndroid)
			add("implementation", rxKotlin)
		}
	}

	object JakeWharton {
		const val rxRelay = "com.jakewharton.rxrelay2:rxrelay:2.1.1"

		object RxBinding {
			private const val version = "2.2.0"
			const val core = "com.jakewharton.rxbinding2:rxbinding:$version"
			const val design = "com.jakewharton.rxbinding2:rxbinding-design:$version"
			const val kotlin = "com.jakewharton.rxbinding2:rxbinding-kotlin:$version"
			const val designKotlin = "com.jakewharton.rxbinding2:rxbinding-design-kotlin:$version"
			const val recyclerviewV7Kotlin = "com.jakewharton.rxbinding2:rxbinding-recyclerview-v7-kotlin:$version"
		}
	}

	object Kotlin {
		private const val version = "1.6.10"
		const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:$version"

		object Coroutines {
			private const val version = "1.6.0"
			const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
			const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
			const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
			const val playServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$version"
			//const val rx2 = "org.jetbrains.kotlinx:kotlinx-coroutines-rx2:$version"
		}

	}

	object Facebook {
		const val soloader = "com.facebook.soloader:soloader:0.10.1"
		object Flipper {
			private const val version = "0.91.1"
			const val debug = "com.facebook.flipper:flipper:$version"
			const val release = "com.facebook.flipper:flipper-noop:$version"
			const val networkPlugin = "com.facebook.flipper:flipper-network-plugin:$version"
		}
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
		const val library =  "com.github.ChuckerTeam.Chucker:library:$version"
		const val noop =  "com.github.ChuckerTeam.Chucker:library-no-op:$version"
	}

	object MvRx {
		private const val version = "1.5.1"
		const val mvrx = "com.airbnb.android:mvrx:$version"
		const val testing = "com.airbnb.android:mvrx-testing:$version"
	}

	object Test {
		//Provided by AndroidX!
		//const val jUnit = "junit:junit:4.12"
		const val assertJ = "org.assertj:assertj-core:3.19.0"
		const val mockK = "io.mockk:mockk:1.10.6"
		const val mockKAndroidTest = "io.mockk:mockk-android:1.10.6"
		const val robolectric = "org.robolectric:robolectric:4.3.1"
		//This is an alternative to Espresso IdlingResource
		const val conditionWatcher = "com.azimolabs.conditionwatcher:conditionwatcher:0.2"
		object JUnit5 {
			private const val version = "5.7.1"
			const val jupiterApi = "org.junit.jupiter:junit-jupiter-api:$version"
			const val jupiterEngine = "org.junit.jupiter:junit-jupiter-engine:$version"
			const val jupiterParams = "org.junit.jupiter:junit-jupiter-params:$version"
			const val vintageEngine = "org.junit.jupiter:junit-vintage-engine:$version"
		}
	}

	object Kotest {
		private const val version = "4.4.1"
		const val runner = "io.kotest:kotest-runner-junit5:$version"
		const val assertions = "io.kotest:kotest-assertions-core:$version"
		const val property = "io.kotest:kotest-property:$version"
	}

	object Detekt {
		private const val version = "1.19.0"
		const val api = "io.gitlab.arturbosch.detekt:detekt-api:$version"
		const val test = "io.gitlab.arturbosch.detekt:detekt-test:$version"
	}

	object Views {
		const val multiStateView = "com.github.Kennyc1012:MultiStateView:2.2.0"
	}
}
