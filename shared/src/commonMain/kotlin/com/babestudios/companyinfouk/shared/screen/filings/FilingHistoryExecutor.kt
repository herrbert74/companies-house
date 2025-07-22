package com.babestudios.companyinfouk.shared.screen.filings

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category
import com.babestudios.companyinfouk.shared.screen.filings.FilingHistoryStore.Intent
import com.babestudios.companyinfouk.shared.screen.filings.FilingHistoryStore.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilingHistoryExecutor(
	private val companiesRepository: CompaniesRepository,
	val mainContext: CoroutineDispatcher,
	private val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, Nothing>(mainContext) {

	override fun executeAction(action: BootstrapIntent) {
		when (action) {
			is BootstrapIntent.LoadFilingHistory -> {
				companiesRepository.logScreenView("FilingHistoryFragment")
				fetchFilingHistory(action.selectedCompanyId)
			}
		}
	}

	override fun executeIntent(intent: Intent) {
		when (intent) {
			is Intent.LoadMoreFilingHistory -> loadMoreFilingHistory(state())
			is Intent.FilingHistoryCategorySelected -> fetchFilingHistory(
				state().selectedCompanyId,
				Category.entries[intent.categoryOrdinal]
			)
		}
	}

	//region filingHistory actions

	private fun fetchFilingHistory(selectedCompanyId: String, category: Category = Category.CATEGORY_SHOW_ALL) {
		scope.launch(ioContext) {
			val filingHistoryResponse = companiesRepository.getFilingHistory(
				selectedCompanyId,
				category,
				"0"
			)
			withContext(mainContext) {
				dispatch(Message.FilingHistoryMessage(filingHistoryResponse, selectedCompanyId, category))
			}
		}
	}

	private fun loadMoreFilingHistory(state: State) {
		if (state.filingHistory.items.size < state.filingHistory.totalCount) {
			scope.launch {
				val filingHistory = companiesRepository.getFilingHistory(
					state.selectedCompanyId,
					state.filingCategoryFilter,
					(state.filingHistory.items.size).toString()
				)
				dispatch(Message.LoadMoreFilingHistoryMessage(filingHistory, state.selectedCompanyId))
			}
		}
	}

	//endregion

}
