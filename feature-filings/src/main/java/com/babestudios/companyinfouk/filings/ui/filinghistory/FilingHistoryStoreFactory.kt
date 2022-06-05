package com.babestudios.companyinfouk.filings.ui.filinghistory

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.babestudios.companyinfouk.domain.model.common.ApiResult
import com.babestudios.companyinfouk.domain.model.filinghistory.Category
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.filings.ui.filinghistory.FilingHistoryStore.Intent
import com.babestudios.companyinfouk.filings.ui.filinghistory.FilingHistoryStore.State
import com.github.michaelbull.result.fold

class FilingsHistoryStoreFactory(
	private val storeFactory: StoreFactory,
	private val filingHistoryExecutor: FilingHistoryExecutor
) {

	fun create(companyNumber: String): FilingHistoryStore =
		object : FilingHistoryStore, Store<Intent, State, SideEffect> by storeFactory.create(
			name = "FilingsHistoryDetailsStore",
			initialState = State.Loading,
			bootstrapper = FilingsHistoryBootstrapper(companyNumber),
			executorFactory = { filingHistoryExecutor },
			reducer = FilingsHistoryReducer
		) {}

	private class FilingsHistoryBootstrapper(val companyNumber: String) : CoroutineBootstrapper<BootstrapIntent>
	() {
		override fun invoke() {
			dispatch(BootstrapIntent.LoadFilingHistory(companyNumber))
		}
	}

	private object FilingsHistoryReducer : Reducer<State, Message> {
		override fun State.reduce(msg: Message): State =
			when (msg) {
				is Message.FilingHistoryMessage -> msg.chargesResult.fold(
					success = {
						State.Show(
							companyNumber = msg.companyNumber,
							filingHistory = it,
							filingCategoryFilter = msg.filingCategoryFilter,
						)
					},
					failure = { State.Error(it) }
				)
				is Message.LoadMoreFilingHistoryMessage -> msg.chargesResult.fold(
					success = {
						State.Show(
							companyNumber = msg.companyNumber,
							filingHistory = FilingHistory(
								items = (this as State.Show).filingHistory.items.plus(it.items),
								totalCount = it.totalCount
							),
							filingCategoryFilter = msg.filingCategoryFilter,
						)
					},
					failure = { State.Error(it) }
				)
			}
	}
}

sealed class BootstrapIntent {
	data class LoadFilingHistory(val companyNumber: String) : BootstrapIntent()
}

sealed class Message {
	data class FilingHistoryMessage(
		val chargesResult: ApiResult<FilingHistory>,
		val companyNumber: String,
		val filingCategoryFilter: Category,
	) : Message()

	data class LoadMoreFilingHistoryMessage(
		val chargesResult: ApiResult<FilingHistory>,
		val companyNumber: String,
		val filingCategoryFilter: Category,
	) : Message()
}
