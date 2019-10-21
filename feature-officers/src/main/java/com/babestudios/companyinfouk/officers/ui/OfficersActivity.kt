package com.babestudios.companyinfouk.officers.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.airbnb.mvrx.MvRxView
import com.babestudios.base.mvrx.BaseActivity
import com.babestudios.companyinfouk.core.injection.CoreInjectHelper
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.navigation.features.OfficersNavigator
import com.babestudios.companyinfouk.officers.R

const val COMPANY_NUMBER: String = "COMPANY_NUMBER"

class OfficersActivity : BaseActivity(), MvRxView {

	override val mvrxViewId: String
		get() = "OfficersActivity"

	private lateinit var comp: OfficersComponent

	private lateinit var companyNumber: String

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_officers)
		companyNumber = intent.getStringExtra(COMPANY_NUMBER).orEmpty()
		comp = DaggerOfficersComponent
				.builder()
				.coreComponent(CoreInjectHelper.provideCoreComponent(applicationContext))
				.build()
	}

	fun provideCompanyNumber(): String {
		return companyNumber
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

fun Context.createOfficersIntent(companyNumber: String): Intent {
	return Intent(this, OfficersActivity::class.java)
			.putExtra(COMPANY_NUMBER, companyNumber)
}
