package com.babestudios.companyinfouk

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.arkivanov.decompose.DefaultComponentContext
import com.babestudios.companyinfouk.companies.ui.main.MainExecutor
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

@AndroidEntryPoint
class CompaniesActivity : AppCompatActivity() {

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

}
