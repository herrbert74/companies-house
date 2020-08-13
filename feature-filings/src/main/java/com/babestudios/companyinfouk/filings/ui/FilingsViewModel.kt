package com.babestudios.companyinfouk.filings.ui

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.appendAt
import com.babestudios.base.mvrx.BaseViewModel
import com.babestudios.companyinfouk.common.model.filinghistory.Category
import com.babestudios.companyinfouk.common.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.filings.ui.filinghistory.list.FilingHistoryVisitable
import com.babestudios.companyinfouk.navigation.features.FilingsNavigator

class FilingsViewModel(
		filingsState: FilingsState,
		private val companiesRepository: CompaniesRepositoryContract,
		val filingsNavigator: FilingsNavigator
) : BaseViewModel<FilingsState>(filingsState, companiesRepository) {

	companion object : MvRxViewModelFactory<FilingsViewModel, FilingsState> {

		@JvmStatic
		override fun create(viewModelContext: ViewModelContext, state: FilingsState): FilingsViewModel? {
			val companiesRepository = viewModelContext.activity<FilingsActivity>().injectCompaniesHouseRepository()
			val filingsNavigator = viewModelContext.activity<FilingsActivity>().injectFilingsNavigator()
			return FilingsViewModel(
					state,
					companiesRepository,
					filingsNavigator
			)
		}

		override fun initialState(viewModelContext: ViewModelContext): FilingsState? {
			val companyNumber = viewModelContext.activity<FilingsActivity>().provideCompanyNumber()
			return if (companyNumber.isNotEmpty())
				FilingsState(companyNumber = companyNumber)
			else
				null
		}
	}

	//region filings

	fun getFilingHistory() {
		withState { state ->
			companiesRepository.getFilingHistory(
					state.companyNumber,
					state.filingCategoryFilter,
					"0"
			)
					.execute {
						copy(
								filingsRequest = it,
								filingsHistory = convertToVisitables(it()),
								totalFilingsCount = it()?.totalCount ?: 0
						)
					}
		}
	}


	fun loadMoreFilingHistory(page: Int) {
		withState { state ->
			if (state.filingsHistory.size < state.totalFilingsCount) {
				companiesRepository.getFilingHistory(
						state.companyNumber,
						state.filingCategoryFilter,
						(page * Integer
								.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE))
								.toString()
				).execute {
					copy(
							filingsRequest = it,
							filingsHistory = filingsHistory.appendAt(
									convertToVisitables(it()),
									filingsHistory.size + 1
							),
							totalFilingsCount = it()?.totalCount ?: 0
					)
				}
			}
		}
	}

	private fun convertToVisitables(reply: FilingHistory?): List<FilingHistoryVisitable> {
		return ArrayList(reply?.items?.map { item -> FilingHistoryVisitable(item) } ?: emptyList())
	}

	fun setCategoryFilter(category: Int) {
		setState {
			copy(
					filingCategoryFilter = Category.values()[category],
					filingsHistory = emptyList()
			)
		}
		getFilingHistory()
	}

	fun filingHistoryItemClicked(adapterPosition: Int) {
		withState { state ->
			val filingHistoryItem = state.filingsHistory[adapterPosition].filingHistoryItem
			setState {
				copy(
						filingHistoryItem = filingHistoryItem
				)
			}
		}
		filingsNavigator.filingsToFilingsDetails()
	}

	//endregion

	//region filings details

	fun fetchDocument() {
		withState { state ->
			state.filingHistoryItem.links.documentMetadata.also { data ->
				//val documentId = it.replace("https://document-api.companieshouse.gov.uk/document/", "")
				val documentId = data
						.replace("https://frontend-doc-api.companieshouse.gov.uk/document/", "")
				companiesRepository.getDocument(documentId)
						.execute {
							copy(
									documentRequest = it,
									pdfResponseBody = it()
							)
						}
			}
		}
	}

	fun writeDocument() {
		withState { state ->
			state.pdfResponseBody?.let { pdfResponseBody ->
				companiesRepository.writeDocumentPdf(pdfResponseBody)
						.execute {
							copy(
									writeDocumentRequest = it,
									pdfUri = it()
							)
						}
			}
		}
	}

	fun resetState() {
		setState {
			copy(
					documentRequest = Uninitialized,
					writeDocumentRequest = Uninitialized
			)
		}
	}

	//endregion
}

fun String?.createSpannableDescription(): Spannable? {
	var s = this
	if (s != null) {
		val firstOpen = s.indexOf("**")
		s = s.replaceFirst("**", "")
		val firstClose = s.indexOf("**", firstOpen + 1)
		s = s.replaceFirst("**", "")
		val secondOpen = s.indexOf("**")
		s = s.replaceFirst("**", "")
		val secondClose = s.indexOf("**", firstOpen + 1)
		s = s.replaceFirst("**", "")
		val spannable = SpannableString(s)
		if (firstOpen > -1) {
			val boldSpan = StyleSpan(Typeface.BOLD)
			spannable.setSpan(boldSpan, firstOpen, firstClose, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
			if (secondOpen > -1) {
				spannable.setSpan(boldSpan, secondOpen, secondClose, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
			}
		}
		return spannable
	}
	return null
}
