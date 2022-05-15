package com.babestudios.companyinfouk

import com.babestudios.companyinfouk.di.AndroidTestDataModule
import com.babestudios.companyinfouk.di.DaggerAndroidTestCoreComponent

open class TestCompaniesHouseApplication : CompaniesHouseApplication() {

	private lateinit var coreComponent: AndroidTestCoreComponent

	override fun provideCoreComponent(): CoreComponent {

		if (!this::coreComponent.isInitialized) {
			coreComponent = DaggerAndroidTestCoreComponent
					.builder()
					.navigationComponent(CompaniesHouseNavigation())
					.androidTestDataModule(AndroidTestDataModule(this))
					.build()
		}
		return coreComponent
	}
}
