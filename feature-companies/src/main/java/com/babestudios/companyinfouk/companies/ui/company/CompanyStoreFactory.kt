package com.babestudios.companyinfouk.companies.ui.company

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.babestudios.companyinfouk.companies.ui.Configuration
import com.babestudios.companyinfouk.companies.ui.company.CompanyStore.Intent
import com.babestudios.companyinfouk.companies.ui.company.CompanyStore.State
import com.babestudios.companyinfouk.domain.model.company.Company

class CompanyStoreFactory(
	private val storeFactory: StoreFactory,
	private val companyExecutor: CompanyExecutor,
) {

	fun create(companyId: String, previousConfig: Configuration): CompanyStore =
		object : CompanyStore, Store<Intent, State, Nothing> by storeFactory.create(
			name = "CompanyStore",
			initialState = State(companyId, previousConfig),
			bootstrapper = CompanyBootstrapper(companyId),
			executorFactory = { companyExecutor },
			reducer = CompanyReducer
		) {}

	private class CompanyBootstrapper(val companyId: String) : CoroutineBootstrapper<BootstrapIntent>() {
		override fun invoke() {
			dispatch(BootstrapIntent.LoadCompany(companyId))
		}
	}

	private object CompanyReducer : Reducer<State, Message> {
		override fun State.reduce(msg: Message): State =
			when (msg) {
				is Message.CompanyLoaded -> this.copy(
					isLoading = false,
					company = msg.company,
					isFavourite = msg.isFavourite
				)
				is Message.FlipFavourite -> this.copy(isFavourite = msg.isFavourite)
			}
	}

}

sealed interface Message {
	data class CompanyLoaded(val company: Company, val isFavourite: Boolean) : Message
	data class FlipFavourite(val isFavourite: Boolean) : Message
}

sealed class BootstrapIntent {
	data class LoadCompany(val companyId: String) : BootstrapIntent()
}
