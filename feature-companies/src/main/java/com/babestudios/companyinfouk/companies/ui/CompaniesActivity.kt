package com.babestudios.companyinfouk.companies.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.babestudios.base.ext.isLazyInitialized
import com.babestudios.base.mvrx.BaseActivity
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.core.injection.CoreInjectHelper
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.navigation.features.CompaniesNavigator

class CompaniesActivity : BaseActivity() {

	private val comp by lazy {
		DaggerCompaniesComponent
				.builder()
				.coreComponent(CoreInjectHelper.provideCoreComponent(applicationContext))
				.build()
	}

	private lateinit var navController: NavController

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_companies)
		navController = findNavController(R.id.navHostFragmentCompanies)
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

	fun injectErrorResolver(): ErrorResolver {
		return comp.errorResolver()
	}

	fun injectCompaniesHouseRepository(): CompaniesRepositoryContract {
		return comp.companiesRepository()
	}

	fun injectCompaniesNavigator(): CompaniesNavigator {
		val nav = comp.navigator()
		if (::navController.isInitialized)
			nav.bind(navController)
		return nav
	}

	fun injectRecentSearchesString(): String {
		return getString(R.string.recent_searches)
	}
}
