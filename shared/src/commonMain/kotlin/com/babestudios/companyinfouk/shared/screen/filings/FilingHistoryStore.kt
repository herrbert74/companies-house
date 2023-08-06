package com.babestudios.companyinfouk.shared.screen.filings

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.shared.screen.filings.FilingHistoryStore.Intent
import com.babestudios.companyinfouk.shared.screen.filings.FilingHistoryStore.State

interface FilingHistoryStore : Store<Intent, State, Nothing> {

	sealed class Intent {
		object LoadMoreFilingHistory : Intent()
		data class FilingHistoryCategorySelected(val categoryOrdinal: Int) : Intent()
	}

	data class State(

		//initial data
		val selectedCompanyId: String,

		//result data
		val filingHistory: FilingHistory = FilingHistory(),
		val error: Throwable? = null,

		//state
		val isLoading: Boolean = true,
		val filingCategoryFilter: Category = Category.CATEGORY_SHOW_ALL,

		)

}
