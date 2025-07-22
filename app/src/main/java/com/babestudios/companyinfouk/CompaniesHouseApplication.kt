package com.babestudios.companyinfouk

import android.app.Application
import com.babestudios.companyinfouk.shared.data.androidDataModule
import com.babestudios.companyinfouk.shared.di.coroutineContextModule
import com.babestudios.companyinfouk.shared.di.dataModule
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

open class CompaniesHouseApplication : Application(), KoinComponent {

	// @Inject
	// lateinit var companiesRepository: CompaniesRepository

	// private var currentActivity: AppCompatActivity? = null

	private lateinit var companiesRepository: CompaniesRepository

	override fun onCreate() {
		super.onCreate()
		// if (BuildConfig.DEBUG) {
		// 	plant(Timber.DebugTree())
		// }

		startKoin {
			androidContext(this@CompaniesHouseApplication)
			modules(androidDataModule, dataModule, coroutineContextModule)
		}

		companiesRepository = getKoin().get()

		logAppOpen(companiesRepository)
	}

	private fun logAppOpen(companiesRepository: CompaniesRepository) {
		companiesRepository.logAppOpen()
	}

	// override fun getCurrentActivity(): AppCompatActivity? {
	// 	return currentActivity
	// }
	//
	// override fun setCurrentActivity(mCurrentActivity: AppCompatActivity) {
	// 	this.currentActivity = mCurrentActivity
	// }
}
