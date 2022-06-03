package com.babestudios.companyinfouk.insolvencies.ui.details

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.data.utils.StringResourceHelper
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.insolvency.Practitioner
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import com.babestudios.companyinfouk.insolvencies.ui.details.InsolvencyDetailsStore.Intent
import com.babestudios.companyinfouk.insolvencies.ui.details.InsolvencyDetailsStore.State
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsDateItem
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsDateVisitable
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsPractitionerItem
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsPractitionerVisitable
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsTitleItem
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsTitleVisitable
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsVisitableBase
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class InsolvencyDetailsExecutor @Inject constructor(
	private val companiesRepository: CompaniesRepository,
	private val stringResourceHelper: StringResourceHelper,
	@MainDispatcher val mainContext: CoroutineDispatcher,
	@IoDispatcher val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, SideEffect>(mainContext) {

	override fun executeAction(action: BootstrapIntent, getState: () -> State) {
		when (action) {
			is BootstrapIntent.ShowInsolvencyCase -> {
				companiesRepository.logScreenView("InsolvenciesDetailsFragment")
				dispatch(
					Message.ShowInsolvencyCase(
						action.companyNumber,
						action.selectedInsolvencyCase,
						stringResourceHelper.getInsolvencyDatesString(),
						stringResourceHelper.getPractitionerString()
					)
				)
			}
		}
	}

	override fun executeIntent(intent: Intent, getState: () -> State) {
		when (intent) {
			is Intent.PractitionerClicked -> practitionerClicked(getState, intent.selectedPractitioner)
		}
	}

	private fun practitionerClicked(getState: () -> State, selectedPractitioner: Practitioner) {
		scope.launch {
			publish(
				SideEffect.PractitionerClicked((getState() as State.Show).companyNumber, selectedPractitioner)
			)
		}
	}

}
