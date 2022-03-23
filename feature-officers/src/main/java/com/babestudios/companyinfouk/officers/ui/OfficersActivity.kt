package com.babestudios.companyinfouk.officers.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.babestudios.base.ext.isLazyInitialized
import com.babestudios.base.mvrx.BaseActivity
import com.babestudios.companyinfouk.core.injection.CoreInjectHelper
import com.babestudios.companyinfouk.domain.api.CompaniesRxRepository
import com.babestudios.companyinfouk.navigation.COMPANY_NUMBER
import com.babestudios.companyinfouk.navigation.features.OfficersNavigator
import com.babestudios.companyinfouk.officers.R

class OfficersActivity : BaseActivity() {

	private val comp by lazy {
		DaggerOfficersComponent
				.builder()
				.coreComponent(CoreInjectHelper.provideCoreComponent(applicationContext))
				.build()
	}

	private lateinit var officersNavigator: OfficersNavigator
	private lateinit var navController: NavController

	private lateinit var companyNumber: String

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		companyNumber = intent.getStringExtra(COMPANY_NUMBER).orEmpty()
		setContentView(R.layout.activity_officers)
		navController = findNavController(R.id.navHostFragmentOfficers)
		if (::comp.isLazyInitialized) {
			officersNavigator.bind(navController)
		}
	}

	override fun onBackPressed() {
		if (onBackPressedDispatcher.hasEnabledCallbacks()) {
			onBackPressedDispatcher.onBackPressed()
		} else {
			super.finish()
			overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out)
		}
	}

	fun provideCompanyNumber(): String {
		return if (::companyNumber.isInitialized) companyNumber else ""
	}

	fun injectCompaniesHouseRepository(): CompaniesRxRepository {
		return comp.companiesRepository()
	}

	fun injectOfficersNavigator(): OfficersNavigator {
		officersNavigator = comp.navigator()
		if (::navController.isInitialized)
			officersNavigator.bind(navController)
		return officersNavigator
	}
}
