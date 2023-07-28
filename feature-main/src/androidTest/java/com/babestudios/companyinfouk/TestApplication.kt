package com.babestudios.companyinfouk

import android.app.Application
import com.babestudios.companyinfouk.di.androidTestDataModule
import com.babestudios.companyinfouk.shared.di.coroutineContextModule
import org.koin.core.context.startKoin

class TestApplication : Application() {
	override fun onCreate() {
		super.onCreate()
		startKoin {
			modules(coroutineContextModule, androidTestDataModule)
		}
	}
}
