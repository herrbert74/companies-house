package com.babestudios.companyinfouk

import android.app.Application
import android.content.Context

import com.babestudios.companyinfouk.injection.ApplicationComponent
import com.babestudios.companyinfouk.injection.ApplicationModule
import com.babestudios.companyinfouk.injection.DaggerApplicationComponent
import com.facebook.stetho.Stetho
import com.google.firebase.analytics.FirebaseAnalytics

open class CompaniesHouseApplication : Application() {

	var firebaseAnalytics: FirebaseAnalytics? = null
		private set

	open lateinit var applicationComponent: ApplicationComponent
		internal set

	override fun onCreate() {
		super.onCreate()
		instance = this
		Stetho.initializeWithDefaults(this)
		firebaseAnalytics = FirebaseAnalytics.getInstance(this)
		logAppOpen()
		applicationComponent = DaggerApplicationComponent.builder()
				.applicationModule(ApplicationModule(this))
				.build()
	}

	private fun logAppOpen() {
		firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.APP_OPEN, null)

	}

	companion object {

		lateinit var instance: CompaniesHouseApplication
			private set

		val context: Context
			get() = instance

		@JvmStatic
		fun get(): CompaniesHouseApplication = instance
	}


}

class Injector private constructor() {
	companion object {
		fun get(): ApplicationComponent =
				CompaniesHouseApplication.get().applicationComponent
	}
}
