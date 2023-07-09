package com.babestudios.companyinfouk.charges.ui.charges

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.babestudios.companyinfouk.shared.domain.model.common.ApiResult
import com.babestudios.companyinfouk.shared.domain.model.charges.Charges
import com.babestudios.companyinfouk.charges.ui.charges.ChargesStore.Intent
import com.babestudios.companyinfouk.charges.ui.charges.ChargesStore.State
import com.github.michaelbull.result.fold

class ChargesStoreFactory(
	private val storeFactory: StoreFactory,
	private val chargesExecutor: ChargesExecutor,
) {

	fun create(selectedCompanyId: String, autoInit :Boolean = true): ChargesStore =
		object : ChargesStore, Store<Intent, State, Nothing> by storeFactory.create(
			name = "ChargesStore",
			autoInit = autoInit,
			initialState = State(selectedCompanyId),
			bootstrapper = ChargesBootstrapper(selectedCompanyId),
			executorFactory = { chargesExecutor },
			reducer = ChargesReducer
		) {}

	private class ChargesBootstrapper(val selectedCompanyId: String) : CoroutineBootstrapper<BootstrapIntent>() {
		override fun invoke() {
			dispatch(BootstrapIntent.LoadCharges(selectedCompanyId))
		}
	}

	private object ChargesReducer : Reducer<State, Message> {
		override fun State.reduce(msg: Message): State =
			when (msg) {
				is Message.ChargesMessage -> msg.chargesResult.fold(
					success = { copy(isLoading = false, chargesResponse = it) },
					failure = { copy(isLoading = false, error = it) }
				)

				is Message.LoadMoreChargesMessage -> msg.chargesResult.fold(
					success = {
						copy(
							isLoading = false,
							chargesResponse = Charges(
								items = chargesResponse.items.plus(it.items),
								totalCount = it.totalCount
							)
						)
					},
					failure = { copy(isLoading = false, error = it) }
				)
			}
	}

}

sealed class Message {
	data class ChargesMessage(val chargesResult: ApiResult<Charges>, val selectedCompanyId: String) : Message()
	data class LoadMoreChargesMessage(
		val chargesResult: ApiResult<Charges>,
		val selectedCompanyId: String,
	) : Message()
}

sealed class BootstrapIntent {
	data class LoadCharges(val selectedCompanyId: String) : BootstrapIntent()
}
