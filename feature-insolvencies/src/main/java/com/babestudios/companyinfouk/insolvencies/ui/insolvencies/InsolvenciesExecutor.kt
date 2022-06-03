package com.babestudios.companyinfouk.insolvencies.ui.insolvencies

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsDateItem
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsDateVisitable
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsPractitionerItem
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsPractitionerVisitable
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsTitleItem
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsTitleVisitable
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsVisitableBase
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesStore.Intent
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesStore.State
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class InsolvenciesExecutor @Inject constructor(
	private val companiesRepository: CompaniesRepository,
	@MainDispatcher val mainContext: CoroutineDispatcher,
	@IoDispatcher val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, SideEffect>(mainContext) {

	override fun executeAction(action: BootstrapIntent, getState: () -> State) {
		when (action) {
			is BootstrapIntent.LoadInsolvencies -> {
				companiesRepository.logScreenView("InsolvenciesFragment")
				fetchInsolvencies(action.companyNumber)
			}
		}
	}

	override fun executeIntent(intent: Intent, getState: () -> State) {
		when (intent) {
			is Intent.InsolvencyClicked -> insolvencyItemClicked(getState, intent.selectedInsolvency)
		}
	}

	private fun fetchInsolvencies(companyNumber: String) {
		scope.launch {
			val insolvency = companiesRepository.getInsolvency(companyNumber)
			dispatch(Message.InsolvenciesMessage(insolvency, companyNumber))
		}
	}

	private fun insolvencyItemClicked(getState: () -> State, selectedInsolvencyCase: InsolvencyCase) {
		scope.launch {
			publish(SideEffect.InsolvencyClicked((getState() as State.Show).companyNumber, selectedInsolvencyCase))
		}
	}

}
