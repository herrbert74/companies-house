package com.babestudios.companyinfouk.ui.filinghistory

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import androidx.annotation.VisibleForTesting
import androidx.core.util.Pair
import com.babestudios.base.ext.biLet
import com.babestudios.base.ext.getSerializedName
import com.babestudios.base.mvp.BasePresenter
import com.babestudios.base.mvp.ErrorType
import com.babestudios.base.mvp.Presenter
import com.babestudios.base.rxjava.SingleObserverWrapper
import com.babestudios.companyinfouk.BuildConfig
import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.filinghistory.Category
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryList
import com.babestudios.companyinfouk.ui.filinghistory.list.FilingHistoryVisitable
import com.uber.autodispose.AutoDispose
import io.reactivex.CompletableSource
import javax.inject.Inject

interface FilingHistoryPresenterContract : Presenter<FilingHistoryState, FilingHistoryViewModel> {
	fun setCategoryFilter(category: Int)
	fun loadMoreFilingHistory(page: Int)
}

class FilingHistoryPresenter
@Inject
constructor(var companiesRepository: CompaniesRepository) : BasePresenter<FilingHistoryState, FilingHistoryViewModel>(), FilingHistoryPresenterContract {


	override fun setViewModel(viewModel: FilingHistoryViewModel, lifeCycleCompletable: CompletableSource?) {
		this.viewModel = viewModel
		this.lifeCycleCompletable = lifeCycleCompletable
		viewModel.state.value?.filingHistoryList?.let {
			sendToViewModel {
				it.apply {
					this.isLoading = false
				}
			}
		} ?: run {
			sendToViewModel {
				it.apply {
					this.isLoading = true
				}
			}
			viewModel.state.value?.also {
				getFilingHistory(it.companyNumber, it.filingCategoryFilter)
			}
		}
	}

	@VisibleForTesting
	fun getFilingHistory(companyNumber: String?, category: Category) {
		(category to companyNumber).biLet { _, _companyNumber ->
			companiesRepository.getFilingHistory(_companyNumber, category.getSerializedName(), "0")
					.`as`(AutoDispose.autoDisposable(lifeCycleCompletable))
					.subscribe(object : SingleObserverWrapper<FilingHistoryList>(this) {
						override fun onSuccess(reply: FilingHistoryList) {
							onFilingHistorySuccess(reply)
						}

						override fun onFailed(e: Throwable) {
							onFilingHistoryError(e)
						}
					})
		}
	}

	override fun loadMoreFilingHistory(page: Int) {
		(viewModel to viewModel.state.value?.companyNumber).biLet { vm, companyNumber ->
			companiesRepository.getFilingHistory(
					companyNumber,
					vm.state.value.filingCategoryFilter.getSerializedName(),
					(page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)).toString())
					.subscribe(object : SingleObserverWrapper<FilingHistoryList>(this) {
						override fun onSuccess(reply: FilingHistoryList) {
							onFilingHistorySuccess(reply)
						}

						override fun onFailed(e: Throwable) {
							onFilingHistoryError(e)
						}
					})
		}
	}

	fun onFilingHistoryError(e: Throwable) {
		sendToViewModel {
			it.apply {
				this.isLoading = false
				this.errorType = ErrorType.UNKNOWN
				this.errorMessage = e.localizedMessage
			}
		}
	}

	fun onFilingHistorySuccess(filingHistoryList: FilingHistoryList) {
		sendToViewModel {
			it.apply {
				this.isLoading = false
				this.filingHistoryList?.addAll(convertHistoryListToVisitables(filingHistoryList)) ?: run {
					this.filingHistoryList = convertHistoryListToVisitables(filingHistoryList)
				}
			}
		}
	}

	private fun convertHistoryListToVisitables(filingHistoryList: FilingHistoryList): ArrayList<FilingHistoryVisitable> {
		return ArrayList(filingHistoryList.items.map { item -> FilingHistoryVisitable(item) })
	}

	override fun setCategoryFilter(category: Int) {
		sendToViewModel {
			it.apply {
				this.filingCategoryFilter = Category.values()[category]
				this.filingHistoryList = ArrayList()
				this.isLoading = true
			}
		}
		getFilingHistory(viewModel.state.value?.companyNumber, Category.values()[category])
	}

	companion object {

		fun createSpannableDescription(string: String?, filingHistoryItem: FilingHistoryItem): Spannable? {
			var s = string
			if (s != null) {
				val first = s.indexOf("**")
				val second = s.indexOf("**", first + 1) - 2

				s = s.replace("**", "")

				val spanPairs = ArrayList<Pair<*, *>>()
				var spanFirst: Int
				if (filingHistoryItem.descriptionValues != null) {
					val officerName = filingHistoryItem.descriptionValues?.officerName
					if (officerName != null) {
						s = s.replace("**", "").replace("{officer_name}", officerName)
						spanFirst = s.indexOf(officerName)
						spanPairs.add(Pair(spanFirst, spanFirst + officerName.length))
					}
					val appointmentDate = filingHistoryItem.descriptionValues?.appointmentDate
					if (appointmentDate != null) {
						s = s.replace("{appointment_date}", appointmentDate)
						spanFirst = s.indexOf(appointmentDate)
						spanPairs.add(Pair(spanFirst, spanFirst + appointmentDate.length))
					}
					val madeUpDate = filingHistoryItem.descriptionValues?.madeUpDate
					if (madeUpDate != null) {
						s = s.replace("{made_up_date}", madeUpDate)
						spanFirst = s.indexOf(madeUpDate)
						spanPairs.add(Pair(spanFirst, spanFirst + madeUpDate.length))
					}
					val terminationDate = filingHistoryItem.descriptionValues?.terminationDate
					if (terminationDate != null) {
						s = s.replace("{termination_date}", terminationDate)
						spanFirst = s.indexOf(terminationDate)
						spanPairs.add(Pair(spanFirst, spanFirst + terminationDate.length))
					}
					val newDate = filingHistoryItem.descriptionValues?.newDate
					if (newDate != null) {
						s = s.replace("{new_date}", newDate)
						spanFirst = s.indexOf(newDate)
						spanPairs.add(Pair(spanFirst, spanFirst + newDate.length))
					}
					val changeDate = filingHistoryItem.descriptionValues?.changeDate
					if (changeDate != null) {
						s = s.replace("{change_date}", changeDate)
						spanFirst = s.indexOf(changeDate)
						spanPairs.add(Pair(spanFirst, spanFirst + changeDate.length))
					}
					val oldAddress = filingHistoryItem.descriptionValues?.oldAddress
					if (oldAddress != null) {
						s = s.replace("{old_address}", oldAddress)
						spanFirst = s.indexOf(oldAddress)
						spanPairs.add(Pair(spanFirst, spanFirst + oldAddress.length))
					}
					val newAddress = filingHistoryItem.descriptionValues?.newAddress
					if (newAddress != null) {
						s = s.replace("{new_address}", newAddress)
						spanFirst = s.indexOf(newAddress)
						spanPairs.add(Pair(spanFirst, spanFirst + newAddress.length))
					}
					val formAttached = filingHistoryItem.descriptionValues?.formAttached
					if (formAttached != null) {
						s = s.replace("{form_attached}", formAttached)
						spanFirst = s.indexOf(formAttached)
						spanPairs.add(Pair(spanFirst, spanFirst + formAttached.length))
					}
					val chargeNumber = filingHistoryItem.descriptionValues?.chargeNumber
					if (chargeNumber != null) {
						s = s.replace("{charge_number}", chargeNumber)
						spanFirst = s.indexOf(chargeNumber)
						spanPairs.add(Pair(spanFirst, spanFirst + chargeNumber.length))
					}
					val chargeCreationDate = filingHistoryItem.descriptionValues?.chargeCreationDate
					if (chargeCreationDate != null) {
						s = s.replace("{charge_creation_date}", chargeCreationDate)
						spanFirst = s.indexOf(chargeCreationDate)
						spanPairs.add(Pair(spanFirst, spanFirst + chargeCreationDate.length))
					}
					val date = filingHistoryItem.descriptionValues?.date
					if (date != null) {
						s = s.replace("{date}", date)
						spanFirst = s.indexOf(date)
						spanPairs.add(Pair(spanFirst, spanFirst + date.length))
					}

				}
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
	}

}
