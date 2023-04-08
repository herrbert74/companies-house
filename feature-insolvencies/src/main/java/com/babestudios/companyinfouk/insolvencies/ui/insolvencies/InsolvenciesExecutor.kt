package com.babestudios.companyinfouk.insolvencies.ui.insolvencies

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesStore.State
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InsolvenciesExecutor @Inject constructor(
	private val companiesRepository: CompaniesRepository,
	val mainContext: CoroutineDispatcher,
	private val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Nothing, BootstrapIntent, State, Message, Nothing>(mainContext) {

	override fun executeAction(action: BootstrapIntent, getState: () -> State) {
		when (action) {
			is BootstrapIntent.LoadInsolvencies -> {
				companiesRepository.logScreenView("InsolvenciesFragment")
				fetchInsolvencies(action.selectedCompanyId)
			}
		}
	}

	//region insolvencies actions

	private fun fetchInsolvencies(selectedCompanyId: String) {
		scope.launch(ioContext) {
			val insolvenciesResponse = companiesRepository.getInsolvency(selectedCompanyId)
			withContext(mainContext) {
				dispatch(Message.InsolvenciesMessage(insolvenciesResponse, selectedCompanyId))
			}
		}
	}

	//endregion

}
