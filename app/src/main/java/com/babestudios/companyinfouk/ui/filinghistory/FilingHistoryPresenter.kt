package com.babestudios.companyinfouk.ui.filinghistory

import android.graphics.Typeface
import android.support.annotation.VisibleForTesting
import android.support.v4.util.Pair
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import com.babestudios.companyinfouk.BuildConfig
import com.babestudios.companyinfouk.data.DataManager
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryList
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import net.grandcentrix.thirtyinch.TiPresenter
import java.util.*
import javax.inject.Inject


class FilingHistoryPresenter @Inject
constructor(internal var dataManager: DataManager) : TiPresenter<FilingHistoryActivityView>(), Observer<FilingHistoryList> {

	private var filingHistoryList: FilingHistoryList? = null

	private var categoryFilter = CategoryFilter.CATEGORY_SHOW_ALL

	enum class CategoryFilter(private val name2: String) {
		CATEGORY_SHOW_ALL("all"),
		CATEGORY_GAZETTE("gazette"),
		CATEGORY_CONFIRMATION_STATEMENT("confirmation-statement"),
		CATEGORY_ACCOUNTS("accounts"),
		CATEGORY_ANNUAL_RETURN("annual return"),
		CATEGORY_OFFICERS("officers"),
		CATEGORY_ADDRESS("address"),
		CATEGORY_CAPITAL("capital"),
		CATEGORY_INSOLVENCY("insolvency"),
		CATEGORY_OTHER("other"),
		CATEGORY_INCORPORATION("incorporation"),
		CATEGORY_CONSTITUTION("change-of-constitution"),
		CATEGORY_AUDITORS("auditors"),
		CATEGORY_RESOLUTION("resolution"),
		CATEGORY_MORTGAGE("mortgage");

		override fun toString(): String {
			return name2
		}
	}

	override fun onAttachView(view: FilingHistoryActivityView) {
		super.onAttachView(view)
		if (filingHistoryList != null) {
			view.showFilingHistory(filingHistoryList!!, categoryFilter)
			view.setInitialCategoryFilter(categoryFilter)
		} else {
			view.showProgress()
			getFilingHistory(view.companyNumber, view.filingCategory)
		}
	}

	@VisibleForTesting
	fun getFilingHistory(companyNumber: String, category: String?) {
		dataManager.getFilingHistory(companyNumber, category ?: "", "0").subscribe(this)
	}

	fun loadMoreFilingHistory(page: Int) {
		dataManager.getFilingHistory(view!!.companyNumber, view!!.filingCategory
				?: "", (page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)).toString()).subscribe(this)
	}

	override fun onComplete() {}

	override fun onSubscribe(d: Disposable) {

	}

	override fun onError(e: Throwable) {
		view?.showError()
		view?.hideProgress()
	}

	override fun onNext(filingHistoryList: FilingHistoryList) {
		this.filingHistoryList = filingHistoryList
		view?.showFilingHistory(filingHistoryList, categoryFilter)
	}

	fun setCategoryFilter(category: Int) {
		categoryFilter = CategoryFilter.values()[category]
		view?.setFilterOnAdapter(categoryFilter)
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
				spannable.setSpan(boldSpan, first, second, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
				for (pair in spanPairs) {
					val boldSpan2 = StyleSpan(Typeface.BOLD)
					if (pair.first as Int > -1 && pair.second as Int > -1) {
						spannable.setSpan(boldSpan2, pair.first as Int, pair.second as Int, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
					}
				}
				return spannable
			}
			return null
		}
	}

}
