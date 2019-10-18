package com.babestudios.companyinfouk.officers.ui

import android.content.Context
import android.os.Bundle
import com.airbnb.mvrx.MvRxView
import com.babestudios.base.mvrx.BaseActivity
import com.babestudios.companyinfouk.core.injection.CoreInjectHelper
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.navigation.features.OfficersNavigator
import com.babestudios.companyinfouk.officers.R

class OfficersActivity : BaseActivity(), MvRxView {
	override val mvrxViewId: String
		get() = "OfficersActivity"

	private lateinit var comp: OfficersComponent

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_officers)
		comp = DaggerOnboardingComponent
				.builder()
				.coreComponent(CoreInjectHelper.provideCoreComponent(applicationContext))
				.build()
	}

	fun injectContext(): Context {
		return comp.context()
	}

	fun injectCompaniesHouseRepository(): CompaniesRepositoryContract {
		return comp.companiesRepository()
	}

	fun injectOfficersNavigator(): OfficersNavigator {
		return comp.navigator()
	}

	override fun invalidate() {

	}
}