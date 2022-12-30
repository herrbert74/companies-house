package com.babestudios.companyinfouk.filings.ui.filings

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.babestudios.companyinfouk.domain.model.common.ApiResult
import com.babestudios.companyinfouk.domain.model.filinghistory.Category
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.filings.ui.filings.FilingHistoryStore.Intent
import com.babestudios.companyinfouk.filings.ui.filings.FilingHistoryStore.State
import com.github.michaelbull.result.fold

class FilingHistoryStoreFactory(
	private val storeFactory: StoreFactory,
	private val filingHistoryExecutor: FilingHistoryExecutor,
) {

	fun create(selectedCompanyId: String, autoInit :Boolean = true): FilingHistoryStore =
		object : FilingHistoryStore, Store<Intent, State, Nothing> by storeFactory.create(
			name = "FilingHistoryStore",
			autoInit = autoInit,
			initialState = State(selectedCompanyId),
			bootstrapper = FilingHistoryBootstrapper(selectedCompanyId),
			executorFactory = { filingHistoryExecutor },
			reducer = FilingHistoryReducer
		) {}

	private class FilingHistoryBootstrapper(val selectedCompanyId: String) : CoroutineBootstrapper<BootstrapIntent>() {
		override fun invoke() {
			dispatch(BootstrapIntent.LoadFilingHistory(selectedCompanyId))
		}
	}

	private object FilingHistoryReducer : Reducer<State, Message> {
		override fun State.reduce(msg: Message): State =
			when (msg) {
				is Message.FilingHistoryMessage -> msg.filingHistoryResult.fold(
					success = { copy(isLoading = false, filingHistory = it, filingCategoryFilter = msg.category) },
					failure = { copy(isLoading = false, error = it, filingCategoryFilter = msg.category) }
				)

				is Message.LoadMoreFilingHistoryMessage -> msg.filingHistoryResult.fold(
					success = {
						copy(
							isLoading = false,
							filingHistory = FilingHistory(
								items = filingHistory.items.plus(it.items),
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
	data class FilingHistoryMessage(
		val filingHistoryResult: ApiResult<FilingHistory>,
		val selectedCompanyId: String,
		val category: Category,
	) : Message()

	data class LoadMoreFilingHistoryMessage(
		val filingHistoryResult: ApiResult<FilingHistory>,
		val selectedCompanyId: String,
	) : Message()
}

sealed class BootstrapIntent {
	data class LoadFilingHistory(val selectedCompanyId: String) : BootstrapIntent()
}
