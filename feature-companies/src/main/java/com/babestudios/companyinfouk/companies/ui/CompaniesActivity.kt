package com.babestudios.companyinfouk.companies.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.babestudios.base.mvrx.BaseActivity
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.navigation.NavigationFlow
import com.babestudios.companyinfouk.navigation.ToFlowNavigatable
import com.babestudios.companyinfouk.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompaniesActivity : BaseActivity(), ToFlowNavigatable {

	private lateinit var navController: NavController

	private var navigation: Navigation = Navigation()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_companies)
		navController = findNavController(R.id.navHostFragmentCompanies)
		navigation.bind(navController)
	}

	override fun onBackPressed() {
		if (onBackPressedDispatcher.hasEnabledCallbacks()) {
			onBackPressedDispatcher.onBackPressed()
		} else {
			super.finish()
			overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out)
		}
	}

	override fun navigateToFlow(flow: NavigationFlow) {
		navigation.navigateToFlow(flow)
	}

//	fun popBackStack() {
//		findNavController(R.id.navHostFragmentCompanies)
//	}

}
