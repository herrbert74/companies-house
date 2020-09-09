package com.babestudios.companyinfouk

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.babestudios.base.mvrx.LifeCycleApp
import com.babestudios.companyinfouk.core.injection.CoreComponent
import com.babestudios.companyinfouk.core.injection.CoreComponentProvider
import com.babestudios.companyinfouk.core.injection.DaggerCoreComponent
import com.babestudios.companyinfouk.data.di.DaggerDataComponent
import com.babestudios.companyinfouk.data.di.DataModule
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.soloader.SoLoader

open class CompaniesHouseApplication : Application(), CoreComponentProvider, LifeCycleApp {

	private var currentActivity: AppCompatActivity? = null

	private lateinit var coreComponent: CoreComponent

	override fun onCreate() {
		super.onCreate()
		SoLoader.init(this, false)

		if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
			val client = AndroidFlipperClient.getInstance(this)
			client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
			client.start()
		}
		logAppOpen()
	}

	private fun logAppOpen() {
		provideCoreComponent().companiesRepository().logAppOpen()
	}

	override fun provideCoreComponent(): CoreComponent {

		if (!this::coreComponent.isInitialized) {
			val dataComponent = DaggerDataComponent
					.factory()
					.create(DataModule(this), this)
			coreComponent = DaggerCoreComponent
					.factory()
					.create(dataComponent, CompaniesHouseNavigation(), this)
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
