package com.babestudios.companyinfouk

import android.content.Context

import com.babestudios.companyinfouk.injection.AndroidTestApplicationComponent
import com.babestudios.companyinfouk.injection.AndroidTestApplicationModule
import com.babestudios.companyinfouk.injection.DaggerAndroidTestApplicationComponent

class TestCompaniesHouseApplication : CompaniesHouseApplication() {

	private lateinit var testApplicationComponent: AndroidTestApplicationComponent

	init {
		instance = this
	}

	override fun onCreate() {
		super.onCreate()
		testApplicationComponent = DaggerAndroidTestApplicationComponent.builder()
				.androidTestApplicationModule(AndroidTestApplicationModule(this))
				.build()
		testApplicationComponent.inject(this)
	}

	/*override fun getApplicationComponent(): AndroidTestApplicationComponent {
		return testApplicationComponent
	}*/

	companion object {

		lateinit var instance: TestCompaniesHouseApplication

		val context: Context
			get() = instance
	}

}
