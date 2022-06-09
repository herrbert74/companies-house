package com.babestudios.companyinfouk.companies.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.babestudios.base.mvrx.BaseActivity
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.domain.api.CompaniesRxRepository
import com.babestudios.companyinfouk.navigation.NavigationFlow
import com.babestudios.companyinfouk.navigation.ToFlowNavigatable
import com.babestudios.companyinfouk.navigation.base.Navigation
import com.babestudios.companyinfouk.navigation.features.CompaniesBaseNavigatable
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CompaniesActivity : BaseActivity(), ToFlowNavigatable {

	@Inject
	lateinit var companiesNavigator: CompaniesBaseNavigatable

	@Inject
	lateinit var companiesRepository: CompaniesRxRepository

	@Suppress("unused")
	private val viewModel: CompaniesViewModel by viewModels()

	private lateinit var navController: NavController

	private var navigation: Navigation = Navigation()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_companies)
		navController = findNavController(R.id.navHostFragmentCompanies)
		navigation.bind(navController)
		companiesNavigator.bind(navController)
	}

	override fun onBackPressed() {
		if (onBackPressedDispatcher.hasEnabledCallbacks()) {
			onBackPressedDispatcher.onBackPressed()
		} else {
			super.finish()
			overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out)
		}
	}

	fun injectCompaniesHouseRepository(): CompaniesRxRepository {
		return companiesRepository
	}

	fun injectCompaniesNavigator(): CompaniesBaseNavigatable {
		if (::navController.isInitialized) {
			companiesNavigator.bind(navController)
		}
		return companiesNavigator
	}

	fun injectRecentSearchesString(): String {
		return getString(R.string.recent_searches)
	}

	override fun navigateToFlow(flow: NavigationFlow) {
		navigation.navigateToFlow(flow)
	}

//	fun popBackStack() {
//		findNavController(R.id.navHostFragmentCompanies)
//	}

}
