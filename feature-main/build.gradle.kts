plugins {
	id("android-library-convention")
	id("kotlin-parcelize")
	alias(libs.plugins.kotlin.composeCompiler) //TODO Move to Feature plugin
	alias(libs.plugins.mokkery)
}

android {

	namespace = "com.babestudios.companyinfouk.main"

	buildFeatures.compose = true

	defaultConfig {
		testInstrumentationRunner = "com.babestudios.companyinfouk.CompaniesHouseAndroidJUnitRunner"
		testOptions.targetSdk = libs.versions.targetSdkVersion.get().toInt()
	}

	packaging {
		resources.excludes.add("META-INF/LICENSE.md")
		resources.excludes.add("META-INF/LICENSE-notice.md")
	}

}

dependencies {

	api(project(":shared"))

	implementation(project(":feature-charges"))
	implementation(project(":feature-companies"))
	implementation(project(":feature-filings"))
	implementation(project(":feature-insolvencies"))
	implementation(project(":feature-officers"))
	implementation(project(":feature-persons"))

	implementation(project(":common"))

	api(libs.androidx.activity) //Transitive
	api(libs.kotlinx.coroutinesCore) //Transitive

	debugApi(libs.androidx.composeRuntime) //Transitive
	releaseImplementation(libs.androidx.composeRuntime) //Transitive

	implementation(libs.androidx.activityCompose) //Transitive
	implementation(libs.androidx.composeAnimationCore) //Transitive
	implementation(libs.androidx.composeUi)
	implementation(libs.androidx.lifecycleCommon) //Transitive
	implementation(libs.androidx.lifecycleViewmodel) //Transitive
	implementation(libs.androidx.savedState) //Transitive
	implementation(libs.decompose.core)
	implementation(libs.decompose.extensions)
	implementation(libs.koin.core)
	implementation(libs.koin.android)
	implementation(libs.kotlinx.datetime)

	runtimeOnly(libs.androidx.composeUiTooling)

	//Needed for createComposeRule, NOT ONLY for createAndroidComposeRule, as in the docs
	debugRuntimeOnly(libs.androidx.composeUiTestManifest)

	androidTestImplementation(platform(libs.androidx.composeBom))
	androidTestImplementation(platform(libs.firebaseBom))

	androidTestImplementation(libs.androidx.composeUiTestJunit4)
	androidTestImplementation(libs.androidx.testEspresso.core)
	androidTestImplementation(libs.androidx.testRunner)
	androidTestImplementation(libs.androidx.composeUiTest) //Transitive
	androidTestImplementation(libs.androidx.composeUiText) //Transitive
	androidTestImplementation(libs.androidx.testExtJUnit) //Transitive from androidx.compose.ui:ui-test-junit4
	//androidTestImplementation(libs.androidx.testExtJUnitKtx) //For ActivityScenario
	androidTestImplementation(libs.decompose.core)
	androidTestImplementation(libs.essenty.lifecycle)
	androidTestImplementation(libs.firebaseAnalytics)
	androidTestImplementation(libs.gson)
	androidTestImplementation(libs.hamcrest) //Transitive
	androidTestImplementation(libs.jUnit)
	androidTestImplementation(libs.kotlinResult.result)
	androidTestImplementation(libs.mokkery.core)

}
