package com.babestudios.companyinfouk.insolvencies.ui.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.babestudios.companyinfouk.domain.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.insolvencies.ui.details.InsolvencyDetailsStore.Intent
import com.babestudios.companyinfouk.insolvencies.ui.details.InsolvencyDetailsStore.State
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsDateItem
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsDateVisitable
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsPractitionerVisitable
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsTitleItem
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsTitleVisitable
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsVisitableBase

class InsolvencyDetailsStoreFactory(
	private val storeFactory: StoreFactory,
	private val insolvencyDetailsExecutor: InsolvencyDetailsExecutor
) {

	fun create(companyNumber: String, selectedInsolvencyCase: InsolvencyCase): InsolvencyDetailsStore =
		object : InsolvencyDetailsStore, Store<Intent, State, SideEffect> by storeFactory.create(
			name = "InsolvencyDetailsStore",
			initialState = State.Loading,
			bootstrapper = InsolvencyDetailsBootstrapper(companyNumber, selectedInsolvencyCase),
			executorFactory = { insolvencyDetailsExecutor },
			reducer = InsolvencyDetailsReducer
		) {}

	private class InsolvencyDetailsBootstrapper(val companyNumber: String, val selectedInsolvencyCase: InsolvencyCase)
		: CoroutineBootstrapper<BootstrapIntent>() {
		override fun invoke() {
			dispatch(BootstrapIntent.ShowInsolvencyCase(companyNumber, selectedInsolvencyCase))
		}
	}

	private object InsolvencyDetailsReducer : Reducer<State, Message> {
		override fun State.reduce(msg: Message): State =
			when (msg) {
				is Message.ShowInsolvencyCase -> State.Show(
					companyNumber = msg.companyNumber,
					insolvencyDetailVisitables = convertCaseToDetailsVisitables(
						msg.selectedInsolvencyCase,
						msg.insolvencyDatesString,
						msg.practitionersString
					),
					msg.selectedInsolvencyCase.dates.size
				)
			}

		/**
		/ The list of insolvency cases is shown on the main screen, and dates/practitioners are shown on the details screen
		 **/
		private fun convertCaseToDetailsVisitables(
			insolvencyCase: InsolvencyCase,
			insolvencyDatesString: String,
			practitionersString: String
		): List<InsolvencyDetailsVisitableBase> {
			val list: MutableList<InsolvencyDetailsVisitableBase> = mutableListOf()

			list.add(InsolvencyDetailsTitleVisitable(InsolvencyDetailsTitleItem(insolvencyDatesString)))
			for (item in insolvencyCase.dates) {
				list.add(InsolvencyDetailsDateVisitable(InsolvencyDetailsDateItem(item.date, item.type)))
			}
			list.add(InsolvencyDetailsTitleVisitable(InsolvencyDetailsTitleItem(practitionersString)))
			for (item in insolvencyCase.practitioners) {
				list.add(InsolvencyDetailsPractitionerVisitable(item))
			}
			return list.toList()
		}

	}

}

sealed class BootstrapIntent {
	data class ShowInsolvencyCase(
		val companyNumber: String, val selectedInsolvencyCase: InsolvencyCase
	) : BootstrapIntent()
}

sealed class Message {
	data class ShowInsolvencyCase(
		val companyNumber: String,
		val selectedInsolvencyCase: InsolvencyCase,
		val insolvencyDatesString: String,
		val practitionersString: String
	) : Message()
}
