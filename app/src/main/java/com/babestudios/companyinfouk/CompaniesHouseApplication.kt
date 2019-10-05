package com.babestudios.companyinfouk

import android.app.Application
import android.content.Context
import com.babestudios.companyinfouk.core.injection.CoreComponent
import com.babestudios.companyinfouk.core.injection.CoreComponentProvider
import com.babestudios.companyinfo.core.injection.DaggerCoreComponent
import com.babestudios.companyinfouk.data.di.CoreModule
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import com.google.firebase.analytics.FirebaseAnalytics
import io.fabric.sdk.android.Fabric

open class CompaniesHouseApplication : Application(), CoreComponentProvider {

	private lateinit var coreComponent: CoreComponent

	var firebaseAnalytics: FirebaseAnalytics? = null
		private set

	override fun onCreate() {
		super.onCreate()
		instance = this
		Stetho.initializeWithDefaults(this)
		firebaseAnalytics = FirebaseAnalytics.getInstance(this)
		logAppOpen()
		val fabric = Fabric.Builder(this)
				.kits(Crashlytics())
				.debuggable(BuildConfig.DEBUG)
				.build()
		Fabric.with(fabric)
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
	override fun provideCoreComponent(): CoreComponent {

		if (!this::coreComponent.isInitialized) {
			val navigator = Navigator()
			coreComponent = DaggerCoreComponent
					.builder()
					.navigationComponent(navigator)
					.coreModule(CoreModule(this))

					.build()
		}
		return coreComponent
	}
}
