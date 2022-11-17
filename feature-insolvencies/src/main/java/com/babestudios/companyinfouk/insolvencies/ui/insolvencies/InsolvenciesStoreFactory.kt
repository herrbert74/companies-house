package com.babestudios.companyinfouk.insolvencies.ui.insolvencies

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.babestudios.companyinfouk.domain.model.common.ApiResult
import com.babestudios.companyinfouk.domain.model.insolvency.Insolvency
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesStore.Intent
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesStore.State
import com.github.michaelbull.result.fold

class InsolvenciesStoreFactory(
	private val storeFactory: StoreFactory,
	private val insolvenciesExecutor: InsolvenciesExecutor
) {

	fun create(companyNumber: String): InsolvenciesStore =
		object : InsolvenciesStore, Store<Intent, State, SideEffect> by storeFactory.create(
			name = "InsolvenciesStore",
			initialState = State.Loading,
			bootstrapper = InsolvenciesBootstrapper(companyNumber),
			executorFactory = { insolvenciesExecutor },
			reducer = InsolvenciesReducer
		) {}

	private class InsolvenciesBootstrapper(val companyNumber: String) : CoroutineBootstrapper<BootstrapIntent>() {
		override fun invoke() {
			dispatch(BootstrapIntent.LoadInsolvencies(companyNumber))
		}
	}

	private object InsolvenciesReducer : Reducer<State, Message> {
		override fun State.reduce(msg: Message): State =
			when (msg) {
				is Message.InsolvenciesMessage -> msg.insolvenciesResult.fold(
					success = { State.Show(companyNumber = msg.companyNumber, insolvencies = it.cases) },
					failure = { State.Error(it) }
				)
			}
	}
}

sealed class BootstrapIntent {
	data class LoadInsolvencies(val companyNumber: String) : BootstrapIntent()
}

sealed class Message {
	data class InsolvenciesMessage(val insolvenciesResult: ApiResult<Insolvency>, val companyNumber: String) :
		Message()
}
