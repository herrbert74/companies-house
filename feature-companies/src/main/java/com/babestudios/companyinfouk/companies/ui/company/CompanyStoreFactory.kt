package com.babestudios.companyinfouk.companies.ui.company

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.babestudios.companyinfouk.companies.ui.company.CompanyStore.Intent
import com.babestudios.companyinfouk.companies.ui.company.CompanyStore.State
import com.babestudios.companyinfouk.domain.model.company.Company

class CompanyStoreFactory(
	private val storeFactory: StoreFactory,
	private val companyExecutor: CompanyExecutor
) {

	fun create(companyNumber: String): CompanyStore =
		object : CompanyStore, Store<Intent, State, SideEffect> by storeFactory.create(
			name = "CompanyStore",
			initialState = State.Loading,
			bootstrapper = CompanyBootstrapper(companyNumber),
			executorFactory = { companyExecutor },
			reducer = CompanyReducer
		) {}

	private class CompanyBootstrapper(val companyNumber: String) : CoroutineBootstrapper<BootstrapIntent>() {
		override fun invoke() {
			dispatch(BootstrapIntent.FetchCompany(companyNumber))
		}
	}

	private object CompanyReducer : Reducer<State, Message> {
		override fun State.reduce(msg: Message): State =
			when (msg) {
				is Message.CompanyLoaded -> State.Show(
					msg.company.companyNumber, msg.company, msg.isFavourite
				)
				is Message.FlipFavourite -> {
					val state = this as State.Show
					State.Show(state.companyNumber, state.company, msg.isFavourite)
				}
			}
	}
}

sealed class BootstrapIntent {
	data class FetchCompany(val companyNumber: String) : BootstrapIntent()
}

sealed interface Message {
	data class CompanyLoaded(val company: Company, val isFavourite: Boolean) : Message
	data class FlipFavourite(val isFavourite: Boolean) : Message
}
