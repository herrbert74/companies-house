package com.babestudios.companyinfouk

import android.app.Application
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import timber.log.Timber
import timber.log.Timber.Forest.plant


@HiltAndroidApp
open class CompaniesHouseApplication : Application() {

	@Inject
	lateinit var companiesRepository: CompaniesRepository

	//private var currentActivity: AppCompatActivity? = null

	override fun onCreate() {
		super.onCreate()
		logAppOpen()
		if (BuildConfig.DEBUG) {
			plant(Timber.DebugTree())
		}
	}

	private fun logAppOpen() {
		companiesRepository.logAppOpen()
	}

//	override fun getCurrentActivity(): AppCompatActivity? {
//		return currentActivity
//	}
//
//	override fun setCurrentActivity(mCurrentActivity: AppCompatActivity) {
//		this.currentActivity = mCurrentActivity
//	}
}
