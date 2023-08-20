package com.babestudios.companyinfouk.shared

import com.babestudios.companyinfouk.shared.domain.api.CompaniesDocumentRepository
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class RootParameters : KoinComponent {
	val companiesRepository: CompaniesRepository by inject()
	val companiesDocumentRepository: CompaniesDocumentRepository by inject()
	val mainContext: CoroutineDispatcher by inject(named("MainDispatcher"))
	val ioContext: CoroutineDispatcher by inject(named("IoDispatcher"))
}