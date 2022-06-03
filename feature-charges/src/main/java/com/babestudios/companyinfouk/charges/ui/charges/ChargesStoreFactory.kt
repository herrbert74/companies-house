package com.babestudios.companyinfouk.charges.ui.charges

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.babestudios.companyinfouk.charges.ui.charges.ChargesStore.Intent
import com.babestudios.companyinfouk.charges.ui.charges.ChargesStore.State
import com.babestudios.companyinfouk.domain.model.charges.Charges
import com.babestudios.companyinfouk.domain.model.common.ApiResult
import com.github.michaelbull.result.fold

class ChargesStoreFactory(
	private val storeFactory: StoreFactory,
	private val ChargesExecutor: ChargesExecutor
) {

	fun create(companyNumber: String): ChargesStore =
		object : ChargesStore, Store<Intent, State, SideEffect> by storeFactory.create(
			name = "ChargesStore",
			initialState = State.Loading,
			bootstrapper = ChargesBootstrapper(companyNumber),
			executorFactory = { ChargesExecutor },
			reducer = ChargesReducer
		) {}

	private class ChargesBootstrapper(val companyNumber: String) : CoroutineBootstrapper<BootstrapIntent>() {
		override fun invoke() {
			dispatch(BootstrapIntent.LoadCharges(companyNumber))
		}
	}

	private object ChargesReducer : Reducer<State, Message> {
		override fun State.reduce(msg: Message): State =
			when (msg) {
				is Message.ChargesMessage -> msg.chargesResult.fold(
					success = { State.Show(companyNumber = msg.companyNumber, charges = it) },
					failure = { State.Error(it) }
				)
				is Message.LoadMoreChargesMessage -> msg.chargesResult.fold(
					success = {
						State.Show(
							companyNumber = msg.companyNumber,
							charges = Charges(
								items = (this as State.Show).charges.items.plus(it.items),
								totalCount = it.totalCount
							)
						)
					},
					failure = { State.Error(it) }
				)
			}
	}
}

sealed class BootstrapIntent {
	data class LoadCharges(val companyNumber: String) : BootstrapIntent()
}

sealed class Message {
	data class ChargesMessage(val chargesResult: ApiResult<Charges>, val companyNumber: String) : Message()
	data class LoadMoreChargesMessage(
		val chargesResult: ApiResult<Charges>,
		val companyNumber: String
	) : Message()
}
