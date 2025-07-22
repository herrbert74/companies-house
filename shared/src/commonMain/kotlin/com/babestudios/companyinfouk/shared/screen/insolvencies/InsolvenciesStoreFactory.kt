package com.babestudios.companyinfouk.shared.screen.insolvencies

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.babestudios.companyinfouk.shared.domain.model.common.ApiResult
import com.babestudios.companyinfouk.shared.domain.model.insolvency.Insolvency
import com.babestudios.companyinfouk.shared.screen.insolvencies.InsolvenciesStore.State
import com.github.michaelbull.result.fold

class InsolvenciesStoreFactory(
	private val storeFactory: StoreFactory,
	private val insolvenciesExecutor: InsolvenciesExecutor,
) {

	fun create(selectedCompanyId: String): InsolvenciesStore = object :
		InsolvenciesStore,
		Store<Nothing, State, Nothing> by storeFactory.create(
			name = "InsolvenciesStore",
			initialState = State(selectedCompanyId),
			bootstrapper = InsolvenciesBootstrapper(selectedCompanyId),
			executorFactory = { insolvenciesExecutor },
			reducer = InsolvenciesReducer
		) {}

	private class InsolvenciesBootstrapper(val selectedCompanyId: String) : CoroutineBootstrapper<BootstrapIntent>() {
		override fun invoke() {
			dispatch(BootstrapIntent.LoadInsolvencies(selectedCompanyId))
		}
	}

	private object InsolvenciesReducer : Reducer<State, Message> {
		override fun State.reduce(msg: Message): State =
			when (msg) {
				is Message.InsolvenciesMessage -> msg.insolvenciesResult.fold(
					success = { copy(isLoading = false, insolvency = it) },
					failure = { copy(isLoading = false, error = it) }
				)
			}
	}

}

sealed class Message {
	data class InsolvenciesMessage(
		val insolvenciesResult: ApiResult<Insolvency>,
		val selectedCompanyId: String,
	) : Message()
}

sealed class BootstrapIntent {
	data class LoadInsolvencies(val selectedCompanyId: String) : BootstrapIntent()
}
