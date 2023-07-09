package com.babestudios.companyinfouk.officers.ui.officers

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.babestudios.companyinfouk.shared.domain.model.common.ApiResult
import com.babestudios.companyinfouk.shared.domain.model.officers.OfficersResponse
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStore.Intent
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStore.State
import com.github.michaelbull.result.fold

class OfficersStoreFactory(
	private val storeFactory: StoreFactory,
	private val officersExecutor: OfficersExecutor,
) {

	fun create(companyId: String, autoInit :Boolean = true): OfficersStore =
		object : OfficersStore, Store<Intent, State, Nothing> by storeFactory.create(
			name = "OfficersStore",
			autoInit = autoInit,
			initialState = State(companyId),
			bootstrapper = OfficersBootstrapper(companyId),
			executorFactory = { officersExecutor },
			reducer = OfficersReducer
		) {}

	private class OfficersBootstrapper(val companyId: String) : CoroutineBootstrapper<BootstrapIntent>() {
		override fun invoke() {
			dispatch(BootstrapIntent.LoadOfficers(companyId))
		}
	}

	private object OfficersReducer : Reducer<State, Message> {
		override fun State.reduce(msg: Message): State =
			when (msg) {
				is Message.OfficersMessage -> msg.officersResult.fold(
					success = { copy(isLoading = false, officersResponse = it) },
					failure = { copy(isLoading = false, error = it) }
				)

				is Message.LoadMoreOfficersMessage -> msg.officersResult.fold(

					success = {
						copy(
							isLoading = false,
							officersResponse = OfficersResponse(
								items = officersResponse.items.plus(it.items),
								totalResults = it.totalResults
							)
						)
					},
					failure = { copy(isLoading = false, error = it) }
				)
			}
	}

}

sealed class Message {
	data class OfficersMessage(val officersResult: ApiResult<OfficersResponse>, val companyId: String) : Message()
	data class LoadMoreOfficersMessage(
		val officersResult: ApiResult<OfficersResponse>,
		val companyId: String,
	) : Message()
}

sealed class BootstrapIntent {
	data class LoadOfficers(val companyId: String) : BootstrapIntent()
}
