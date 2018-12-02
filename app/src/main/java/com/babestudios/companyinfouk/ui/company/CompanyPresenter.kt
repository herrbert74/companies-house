package com.babestudios.companyinfouk.ui.company

import android.util.Log
import com.babestudios.base.ext.biLet
import com.babestudios.companyinfouk.data.DataManager
import com.babestudios.companyinfouk.data.model.company.Company
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver
import net.grandcentrix.thirtyinch.TiPresenter
import javax.inject.Inject

class CompanyPresenter @Inject
constructor(var dataManager: DataManager) : TiPresenter<CompanyActivityView>() {

	var company: Company? = null
	/**
	 * It's safe to retrieve these from the view, because they come from the previous Activity, but not safe to get them from the company field, which might be null.
	 */
	private var companyNumber: String? = null
	private var companyName: String? = null

	private var companyActivityView: CompanyActivityView? = null

	override fun onAttachView(view: CompanyActivityView) {
		super.onAttachView(view)
		companyActivityView = view
		companyName = view.companyName
		companyNumber = view.companyNumber
		company?.let {
			showCompany(it)
		} ?: getCompany(companyNumber)
	}

	fun getCompany(companyNumber: String?) {
		companyActivityView!!.showProgress()
		dataManager.getCompany(companyNumber ?: "")
				.subscribe(object : DisposableObserver<Company>() {
					override fun onComplete() {

					}

					override fun onError(e: Throwable) {
						Log.d("test", "onError: " + e.fillInStackTrace())
						companyActivityView?.showError()
						companyActivityView?.hideProgress()
					}

					override fun onNext(company: Company) {
						company.accounts?.lastAccounts?.type?.let {
							company.accounts?.lastAccounts?.type = dataManager.accountTypeLookup(it)
						}
						this@CompanyPresenter.company = company
						showCompany(company)
					}
				})
	}

	private fun showCompany(company: Company) {
		companyActivityView?.showCompany(company)
		companyActivityView?.hideProgress()
		if (company.sicCodes.isNotEmpty()) {
			companyActivityView?.showNatureOfBusiness(company.sicCodes[0], dataManager.sicLookup(company.sicCodes[0]))
		} else {
			companyActivityView?.showEmptyNatureOfBusiness()
		}
	}

	fun isFavourite(searchHistoryItem: SearchHistoryItem): Boolean {
		return dataManager.isFavourite(searchHistoryItem)
	}

	fun observablesFromViews(o: Observable<Any>) {
		o.subscribe {
			(companyName to companyNumber).biLet { companyName, companyNumber ->
				if (dataManager.isFavourite(SearchHistoryItem(companyName, companyNumber, 0))) {
					dataManager.removeFavourite(SearchHistoryItem(companyName, companyNumber, 0))
				} else {
					dataManager.addFavourite(SearchHistoryItem(companyName, companyNumber, 0))
				}
				companyActivityView?.hideFab()
			}
		}
	}
}