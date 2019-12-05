package com.babestudios.companyinfouk.filings.ui

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.core.util.Pair
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.appendAt
import com.babestudios.base.ext.getSerializedName
import com.babestudios.base.mvrx.BaseViewModel
import com.babestudios.base.mvrx.resolveErrorOrProceed
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.companyinfouk.common.model.filinghistory.CategoryDto
import com.babestudios.companyinfouk.common.model.filinghistory.FilingHistoryDto
import com.babestudios.companyinfouk.common.model.filinghistory.FilingHistoryItemDto
import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.model.filinghistory.convertToDomainModel
import com.babestudios.companyinfouk.filings.ui.filinghistory.list.FilingHistoryVisitable
import com.babestudios.companyinfouk.navigation.features.FilingsNavigator

class FilingsViewModel(
		filingsState: FilingsState,
		private val companiesRepository: CompaniesRepositoryContract,
		val filingsNavigator: FilingsNavigator,
		private val errorResolver: ErrorResolver
) : BaseViewModel<FilingsState>(filingsState, companiesRepository) {

	companion object : MvRxViewModelFactory<FilingsViewModel, FilingsState> {

		@JvmStatic
		override fun create(viewModelContext: ViewModelContext, state: FilingsState): FilingsViewModel? {
			val companiesRepository = viewModelContext.activity<FilingsActivity>().injectCompaniesHouseRepository()
			val filingsNavigator = viewModelContext.activity<FilingsActivity>().injectFilingsNavigator()
			val errorResolver = viewModelContext.activity<FilingsActivity>().injectErrorResolver()
			return FilingsViewModel(
					state,
					companiesRepository,
					filingsNavigator,
					errorResolver
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
					state.filingCategoryFilter.convertToDomainModel().getSerializedName(),
					"0"
			)
					.execute {
						copy(
								filingsRequest = it.resolveErrorOrProceed(errorResolver),
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
						state.filingCategoryFilter.displayName,
						(page * Integer
								.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE))
								.toString()
				).execute {
					copy(
							filingsRequest = it.resolveErrorOrProceed(errorResolver),
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

	private fun convertToVisitables(reply: FilingHistoryDto?): List<FilingHistoryVisitable> {
		return ArrayList(reply?.items?.map { item -> FilingHistoryVisitable(item) } ?: emptyList())
	}

	fun setCategoryFilter(category: Int) {
		setState {
			copy(
					filingCategoryFilter = CategoryDto.values()[category],
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
									documentRequest = it.resolveErrorOrProceed(errorResolver),
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
									writeDocumentRequest = it.resolveErrorOrProceed(errorResolver),
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

fun String?.createSpannableDescription(filingHistoryItem: FilingHistoryItemDto): Spannable? {
	var s = this
	if (s != null) {
		val first = s.indexOf("**")
		val second = s.indexOf("**", first + 1) - 2

		s = s.replace("**", "")

		val spanPairs = ArrayList<Pair<*, *>>()
		var spanFirst: Int
		val officerName = filingHistoryItem.descriptionValues.officerName
		s = s.replace("**", "").replace("{officer_name}", officerName)
		spanFirst = s.indexOf(officerName)
		spanPairs.add(Pair(spanFirst, spanFirst + officerName.length))
		val appointmentDate = filingHistoryItem.descriptionValues.appointmentDate
		s = s.replace("{appointment_date}", appointmentDate)
		spanFirst = s.indexOf(appointmentDate)
		spanPairs.add(Pair(spanFirst, spanFirst + appointmentDate.length))
		val madeUpDate = filingHistoryItem.descriptionValues.madeUpDate
		s = s.replace("{made_up_date}", madeUpDate)
		spanFirst = s.indexOf(madeUpDate)
		spanPairs.add(Pair(spanFirst, spanFirst + madeUpDate.length))
		val terminationDate = filingHistoryItem.descriptionValues.terminationDate
		s = s.replace("{termination_date}", terminationDate)
		spanFirst = s.indexOf(terminationDate)
		spanPairs.add(Pair(spanFirst, spanFirst + terminationDate.length))
		val newDate = filingHistoryItem.descriptionValues.newDate
		s = s.replace("{new_date}", newDate)
		spanFirst = s.indexOf(newDate)
		spanPairs.add(Pair(spanFirst, spanFirst + newDate.length))
		val changeDate = filingHistoryItem.descriptionValues.changeDate
		s = s.replace("{change_date}", changeDate)
		spanFirst = s.indexOf(changeDate)
		spanPairs.add(Pair(spanFirst, spanFirst + changeDate.length))
		val oldAddress = filingHistoryItem.descriptionValues.oldAddress
		s = s.replace("{old_address}", oldAddress)
		spanFirst = s.indexOf(oldAddress)
		spanPairs.add(Pair(spanFirst, spanFirst + oldAddress.length))
		val newAddress = filingHistoryItem.descriptionValues.newAddress
		s = s.replace("{new_address}", newAddress)
		spanFirst = s.indexOf(newAddress)
		spanPairs.add(Pair(spanFirst, spanFirst + newAddress.length))
		val formAttached = filingHistoryItem.descriptionValues.formAttached
		s = s.replace("{form_attached}", formAttached)
		spanFirst = s.indexOf(formAttached)
		spanPairs.add(Pair(spanFirst, spanFirst + formAttached.length))
		val chargeNumber = filingHistoryItem.descriptionValues.chargeNumber
		s = s.replace("{charge_number}", chargeNumber)
		spanFirst = s.indexOf(chargeNumber)
		spanPairs.add(Pair(spanFirst, spanFirst + chargeNumber.length))
		val chargeCreationDate = filingHistoryItem.descriptionValues.chargeCreationDate
		s = s.replace("{charge_creation_date}", chargeCreationDate)
		spanFirst = s.indexOf(chargeCreationDate)
		spanPairs.add(Pair(spanFirst, spanFirst + chargeCreationDate.length))
		val date = filingHistoryItem.descriptionValues.date
		s = s.replace("{date}", date)
		spanFirst = s.indexOf(date)
		spanPairs.add(Pair(spanFirst, spanFirst + date.length))

		val spannable = SpannableString(s)
		val boldSpan = StyleSpan(Typeface.BOLD)
		if (first > -1) {
			spannable.setSpan(boldSpan, first, second, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
			for (pair in spanPairs) {
				val boldSpan2 = StyleSpan(Typeface.BOLD)
				if (pair.first as Int > -1 && pair.second as Int > -1) {
					spannable.setSpan(boldSpan2, pair.first as Int, pair.second as Int, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
				}
			}
		}
		return spannable
	}
	return null
}
