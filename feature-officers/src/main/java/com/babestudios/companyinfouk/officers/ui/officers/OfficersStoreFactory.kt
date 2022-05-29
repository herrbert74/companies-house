package com.babestudios.companyinfouk.officers.ui.officers

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.babestudios.companyinfouk.domain.model.common.ApiResult
import com.babestudios.companyinfouk.domain.model.officers.OfficersResponse
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStore.Intent
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStore.State
import com.github.michaelbull.result.fold

internal class OfficersStoreFactory(
	private val storeFactory: StoreFactory,
	val officersExecutor: OfficersExecutor
) {

	fun create(companyNumber: String): OfficersStore =
		object : OfficersStore, Store<Intent, State, SideEffect> by storeFactory.create(
			name = "OfficersStore",
			initialState = State.Loading,
			bootstrapper = OfficersBootstrapper(companyNumber),
			executorFactory = { officersExecutor },
			reducer = OfficersReducer
		) {
		}

	private object OfficersReducer : Reducer<State, Message> {
		override fun State.reduce(msg: Message): State {
			return when (msg) {
				is Message.OfficersMessage -> msg.officersResult.fold(
					success = { State.Show(companyNumber = msg.companyNumber, officersResponse = it) },
					failure = { State.Error(it) }
				)
			}
		}
	}

	private class OfficersBootstrapper(val companyNumber: String) : CoroutineBootstrapper<BootstrapIntent>() {
		override fun invoke() {
			dispatch(BootstrapIntent.LoadOfficers(companyNumber))
		}
	}
}

sealed class Message {
	data class OfficersMessage(val officersResult: ApiResult<OfficersResponse>, val companyNumber: String) : Message()
}

sealed class BootstrapIntent {
	data class LoadOfficers(val companyNumber: String) : BootstrapIntent()
}
