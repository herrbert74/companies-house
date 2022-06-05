package com.babestudios.companyinfouk.filings.ui.filinghistory

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.domain.model.filinghistory.Category
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.filings.ui.filinghistory.FilingHistoryStore.Intent
import com.babestudios.companyinfouk.filings.ui.filinghistory.FilingHistoryStore.State

interface FilingHistoryStore : Store<Intent, State, SideEffect> {

	sealed class Intent {
		data class FilterClicked(val filingCategoryFilter: Category) : Intent()
		data class FilingHistoryItemClicked(val selectedFilingHistoryItem: FilingHistoryItem) : Intent()
		data class LoadMoreFilingHistory(val page: Int) : Intent()
	}

	sealed class State {

		object Loading : State()

		class Show(
			val companyNumber: String,
			val filingCategoryFilter: Category,
			val filingHistory: FilingHistory,
		) : State()

		class Error(val t: Throwable) : State()

	}

}
