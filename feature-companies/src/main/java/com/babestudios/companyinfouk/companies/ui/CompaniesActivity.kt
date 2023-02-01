package com.babestudios.companyinfouk.companies.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.navigation.Navigation
import com.babestudios.companyinfouk.navigation.NavigationFlow
import com.babestudios.companyinfouk.navigation.ToFlowNavigatable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompaniesActivity : AppCompatActivity(), ToFlowNavigatable {

	private lateinit var navController: NavController

	private var navigation: Navigation = Navigation()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_companies)
		navController = findNavController(R.id.navHostFragmentCompanies)
		navigation.bind(navController)
	}

	override fun navigateToFlow(flow: NavigationFlow) {
		navigation.navigateToFlow(flow)
	}

	override fun popBackStack() {
		navigation.popBackStack()
	}

}
