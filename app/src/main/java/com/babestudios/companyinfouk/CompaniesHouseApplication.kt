package com.babestudios.companyinfouk

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.babestudios.base.mvrx.LifeCycleApp
import com.babestudios.companyinfouk.core.injection.CoreComponent
import com.babestudios.companyinfouk.core.injection.CoreComponentProvider
import com.babestudios.companyinfouk.core.injection.DaggerCoreComponent
import com.babestudios.companyinfouk.data.di.DataModule
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import io.fabric.sdk.android.Fabric

open class CompaniesHouseApplication : Application(), CoreComponentProvider, LifeCycleApp {

	private var currentActivity: AppCompatActivity? = null

	private lateinit var coreComponent: CoreComponent

	override fun onCreate() {
		super.onCreate()
		Stetho.initializeWithDefaults(this)
		logAppOpen()
		val fabric = Fabric.Builder(this)
				.kits(Crashlytics())
				.debuggable(BuildConfig.DEBUG)
				.build()
		Fabric.with(fabric)
	}

	private fun logAppOpen() {
		provideCoreComponent().companiesRepository().logAppOpen()
	}

	override fun provideCoreComponent(): CoreComponent {

		if (!this::coreComponent.isInitialized) {
			coreComponent = DaggerCoreComponent
					.builder()
					.navigationComponent(CompaniesHouseNavigation())
					.dataModule(DataModule(this))
					.build()
		}
		return coreComponent
	}

	override fun getCurrentActivity(): AppCompatActivity? {
		return currentActivity
	}

	override fun setCurrentActivity(mCurrentActivity: AppCompatActivity) {
		this.currentActivity = mCurrentActivity
	}
}
