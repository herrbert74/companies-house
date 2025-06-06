package com.babestudios.companyinfouk.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.DefaultComponentContext
import com.babestudios.companyinfouk.shared.domain.api.CompaniesDocumentRepository
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.root.CompaniesRootComponent
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

class CompaniesActivity : ComponentActivity() {

	private val companiesRepository: CompaniesRepository by inject()
	private val companiesDocumentRepository: CompaniesDocumentRepository by inject()
	private val mainContext: CoroutineDispatcher by inject(named("MainDispatcher"))
	private val ioContext: CoroutineDispatcher by inject(named("IoDispatcher"))

	private lateinit var companiesRootComponent: CompaniesRootComponent

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		companiesRootComponent = CompaniesRootComponent(
			DefaultComponentContext(lifecycle, savedStateRegistry, viewModelStore, null),
			mainContext,
			ioContext,
			companiesRepository,
			companiesDocumentRepository,
		) { finish() }

		setContent {
			CompaniesRootContent(companiesRootComponent)
		}
	}

}
