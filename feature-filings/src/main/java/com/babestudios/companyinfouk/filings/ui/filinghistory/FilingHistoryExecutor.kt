package com.babestudios.companyinfouk.filings.ui.filinghistory

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.common.ApiResult
import com.babestudios.companyinfouk.domain.model.filinghistory.Category
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import com.babestudios.companyinfouk.filings.ui.filinghistory.FilingHistoryStore.Intent
import com.babestudios.companyinfouk.filings.ui.filinghistory.FilingHistoryStore.State
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class FilingHistoryExecutor @Inject constructor(
	private val companiesRepository: CompaniesRepository,
	@MainDispatcher val mainContext: CoroutineDispatcher,
	@IoDispatcher val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, SideEffect>(mainContext) {

	override fun executeAction(action: BootstrapIntent, getState: () -> State) {
		when (action) {
			is BootstrapIntent.LoadFilingHistory -> {
				companiesRepository.logScreenView("FilingHistoryFragment")
				getFilingHistory(action.companyNumber, Category.CATEGORY_SHOW_ALL)
			}
		}
	}

	override fun executeIntent(intent: Intent, getState: () -> State) {
		when (intent) {
			is Intent.FilingHistoryItemClicked -> scope.launch {
				publish(SideEffect.FilingItemClicked(intent.selectedFilingHistoryItem))
			}
			is Intent.FilterClicked -> getFilingHistory(
				(getState() as State.Show).companyNumber,
				intent.filingCategoryFilter
			)
			is Intent.LoadMoreFilingHistory -> loadMoreFilingHistory(
				(getState() as State.Show).companyNumber,
				(getState() as State.Show).filingCategoryFilter,
				intent.page,
				getState() as State.Show,
			)
		}
	}


	//region filings

	private fun getFilingHistory(companyNumber: String, filingCategoryFilter: Category) {
		scope.launch {
			val filingHistory = companiesRepository.getFilingHistory(
				companyNumber,
				filingCategoryFilter,
				"0"
			)
			dispatch(Message.FilingHistoryMessage(filingHistory, companyNumber, filingCategoryFilter))
		}
	}


	private fun loadMoreFilingHistory(
		companyNumber: String,
		filingCategoryFilter: Category,
		page: Int,
		showState: State.Show
	) {
		scope.launch {
			val filingHistory = companiesRepository.getFilingHistory(
				companyNumber,
				filingCategoryFilter,
				(page * Integer
					.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE))
					.toString()
			)
			dispatch(Message.LoadMoreFilingHistoryMessage(
				filterHistory(showState, filingHistory), companyNumber, filingCategoryFilter
			))
		}
	}

	//We need to filter manually sometimes
	//https://bitbucket.org/herrbert74/companies-house/issues/77/filinghistory-problems
	private fun filterHistory(showState: State.Show, filingHistory: ApiResult<FilingHistory>):
		ApiResult<FilingHistory> {
		return filingHistory.onSuccess { history ->
			history.items.filter { item ->
				if (showState.filingCategoryFilter == Category.CATEGORY_CONFIRMATION_STATEMENT)
					item.category == Category.CATEGORY_CONFIRMATION_STATEMENT
				else
					true
			}
		}.onFailure { }
	}

	//endregion

}
