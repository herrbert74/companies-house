package com.babestudios.companyinfouk.officers.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.babestudios.base.ext.isLazyInitialized
import com.babestudios.base.mvrx.BaseActivity
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.companyinfouk.core.injection.CoreInjectHelper
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
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

	private lateinit var navController: NavController

	private lateinit var companyNumber: String

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_officers)
		companyNumber = intent.getStringExtra(COMPANY_NUMBER).orEmpty()
		navController = findNavController(R.id.navHostFragmentOfficers)
		if (::comp.isLazyInitialized) {
			val nav = comp.navigator()
			nav.bind(navController)
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
		return companyNumber
	}

	fun injectErrorResolver(): ErrorResolver {
		return comp.errorResolver()
	}

	fun injectCompaniesHouseRepository(): CompaniesRepositoryContract {
		return comp.companiesRepository()
	}

	fun injectOfficersNavigator(): OfficersNavigator {
		val nav = comp.navigator()
		if (::navController.isInitialized)
			nav.bind(navController)
		return nav
	}
}
