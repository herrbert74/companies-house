package com.babestudios.companyinfouk

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.arkivanov.decompose.DefaultComponentContext
import com.babestudios.companyinfouk.companies.ui.main.MainExecutor
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import com.babestudios.companyinfouk.navigation.Navigation
import com.babestudios.companyinfouk.navigation.NavigationFlow
import com.babestudios.companyinfouk.navigation.ToFlowNavigatable
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

@AndroidEntryPoint
class CompaniesActivity : AppCompatActivity(), ToFlowNavigatable {

	@Inject
	lateinit var mainExecutor: MainExecutor

	@Inject
	lateinit var companiesRepository: CompaniesRepository

	@Inject
	@MainDispatcher
	lateinit var mainContext: CoroutineDispatcher

	@Inject
	@IoDispatcher
	lateinit var ioContext: CoroutineDispatcher

	private lateinit var companiesRootComponent: CompaniesRootComponent

	private var navigation: Navigation = Navigation()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		companiesRootComponent = CompaniesRootComponent(
			DefaultComponentContext(lifecycle, savedStateRegistry, viewModelStore, null),
			mainContext,
			ioContext,
			companiesRepository,
			{ finish() },
			mainExecutor,
		)

		setContent {
			CompaniesRootContent(companiesRootComponent)
		}
	}

	override fun navigateToFlow(flow: NavigationFlow) {
		navigation.navigateToFlow(flow)
	}

	override fun popBackStack() {
		navigation.popBackStack()
	}

}
