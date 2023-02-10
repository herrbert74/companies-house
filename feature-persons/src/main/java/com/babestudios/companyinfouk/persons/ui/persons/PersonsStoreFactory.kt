package com.babestudios.companyinfouk.persons.ui.persons

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.babestudios.companyinfouk.domain.model.common.ApiResult
import com.babestudios.companyinfouk.domain.model.persons.PersonsResponse
import com.babestudios.companyinfouk.persons.ui.persons.PersonsStore.Intent
import com.babestudios.companyinfouk.persons.ui.persons.PersonsStore.State
import com.github.michaelbull.result.fold

class PersonsStoreFactory(
	private val storeFactory: StoreFactory,
	private val personsExecutor: PersonsExecutor
) {

	fun create(companyId: String): PersonsStore =
		object : PersonsStore, Store<Intent, State, Nothing> by storeFactory.create(
			name = "PersonsStore",
			initialState = State(companyId),
			bootstrapper = PersonsBootstrapper(companyId),
			executorFactory = { personsExecutor },
			reducer = PersonsReducer
		) {}

	private class PersonsBootstrapper(val companyNumber: String) : CoroutineBootstrapper<BootstrapIntent>() {
		override fun invoke() {
			dispatch(BootstrapIntent.LoadPersons(companyNumber))
		}
	}

	private object PersonsReducer : Reducer<State, Message> {
		override fun State.reduce(msg: Message): State =
			when (msg) {
				is Message.PersonsMessage -> msg.personsResult.fold(
					success = { copy(isLoading = false, personsResponse = it) },
					failure = { copy(isLoading = false, error = it) }
				)
				is Message.LoadMorePersonsMessage -> msg.personsResult.fold(
					success = {
						copy(
							isLoading = false,
							personsResponse = PersonsResponse(
								items = personsResponse.items.plus(it.items),
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
	data class PersonsMessage(val personsResult: ApiResult<PersonsResponse>, val companyId: String) : Message()
	data class LoadMorePersonsMessage(
		val personsResult: ApiResult<PersonsResponse>,
		val companyNumber: String
	) : Message()
}

sealed class BootstrapIntent {
	data class LoadPersons(val companyNumber: String) : BootstrapIntent()
}
