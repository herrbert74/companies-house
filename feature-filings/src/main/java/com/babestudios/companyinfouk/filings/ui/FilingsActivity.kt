package com.babestudios.companyinfouk.filings.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.babestudios.base.ext.isLazyInitialized
import com.babestudios.base.mvrx.BaseActivity
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.companyinfouk.core.injection.CoreInjectHelper
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.filings.R
import com.babestudios.companyinfouk.navigation.COMPANY_NUMBER
import com.babestudios.companyinfouk.navigation.features.FilingsNavigator

class FilingsActivity : BaseActivity() {

	private val comp by lazy {
		DaggerFilingsComponent
				.builder()
				.coreComponent(CoreInjectHelper.provideCoreComponent(applicationContext))
				.build()
	}

	private lateinit var navController: NavController

	private lateinit var companyNumber: String

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_filings)
		companyNumber = intent.getStringExtra(COMPANY_NUMBER).orEmpty()
		navController = findNavController(R.id.navHostFragmentFilings)
		if (::comp.isLazyInitialized) {
			val nav = comp.navigator()
			nav.bind(navController)
		}
	}

	override fun onBackPressed() {
		super.finish()
		overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out)
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

	fun injectFilingsNavigator(): FilingsNavigator {
		val nav = comp.navigator()
		if (::navController.isInitialized)
			nav.bind(navController)
		return nav
	}
}

fun Context.createFilingsIntent(companyNumber: String): Intent {
	return Intent(this, FilingsActivity::class.java)
			.putExtra(COMPANY_NUMBER, companyNumber)
}
