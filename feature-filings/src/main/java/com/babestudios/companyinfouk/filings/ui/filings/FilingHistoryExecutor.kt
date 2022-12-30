package com.babestudios.companyinfouk.filings.ui.filings

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.filinghistory.Category
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import com.babestudios.companyinfouk.filings.ui.filings.FilingHistoryStore.Intent
import com.babestudios.companyinfouk.filings.ui.filings.FilingHistoryStore.State
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilingHistoryExecutor @Inject constructor(
	private val companiesRepository: CompaniesRepository,
	@MainDispatcher val mainContext: CoroutineDispatcher,
	@IoDispatcher val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, Nothing>(mainContext) {

	override fun executeAction(action: BootstrapIntent, getState: () -> State) {
		when (action) {
			is BootstrapIntent.LoadFilingHistory -> {
				companiesRepository.logScreenView("FilingHistoryFragment")
				fetchFilingHistory(action.selectedCompanyId)
			}
		}
	}

	override fun executeIntent(intent: Intent, getState: () -> State) {
		when (intent) {
			is Intent.LoadMoreFilingHistory -> loadMoreFilingHistory(getState)
			is Intent.FilingHistoryCategorySelected -> fetchFilingHistory(
				getState().selectedCompanyId,
				Category.values()[intent.categoryOrdinal]
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


	private fun loadMoreFilingHistory(getState: () -> State) {
		val showState = getState()
		if (showState.filingHistory.items.size < showState.filingHistory.totalCount) {
			scope.launch {
				val filingHistory = companiesRepository.getFilingHistory(
					showState.selectedCompanyId,
					showState.filingCategoryFilter,
					(showState.filingHistory.items.size).toString()
				)
				dispatch(Message.LoadMoreFilingHistoryMessage(filingHistory, showState.selectedCompanyId))
			}
		}
	}

	//endregion

}
