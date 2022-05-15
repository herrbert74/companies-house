package com.babestudios.companyinfouk.officers.ui.officers

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.babestudios.companyinfouk.domain.model.officers.OfficersResponse
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStore.Intent
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStore.State

internal class OfficersStoreFactory(
	private val storeFactory: StoreFactory,
	val bookmarksExecutor: OfficersExecutor
) {

	fun create(companyNumber: String): OfficersStore =
		object : OfficersStore, Store<Intent, State, SideEffect> by storeFactory.create(
			name = "OfficersStore",
			initialState = State.Loading,
			bootstrapper = OfficersBootstrapper(companyNumber),
			executorFactory = { bookmarksExecutor },
			reducer = OfficersReducer
		) {
		}

	private object OfficersReducer : Reducer<State, Message> {
		override fun State.reduce(msg: Message): State {
			return when (msg) {
				is Message.OfficersMessage -> State.Show(
					companyNumber = msg.companyNumber,
					officers = msg.officersResponse.items,
					totalOfficersCount = msg.officersResponse.totalResults
				)
				is Message.Error -> State.Error(msg.t)
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
	data class OfficersMessage(val officersResponse: OfficersResponse, val companyNumber: String) : Message()
	class Error(val t: Throwable) : Message()
}

sealed class BootstrapIntent {
	data class LoadOfficers(val companyNumber: String) : BootstrapIntent()
}
