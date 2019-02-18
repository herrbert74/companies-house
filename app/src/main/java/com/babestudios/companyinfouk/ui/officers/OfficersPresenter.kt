package com.babestudios.companyinfouk.ui.officers

import androidx.annotation.VisibleForTesting
import android.util.Log

import com.babestudios.companyinfouk.BuildConfig
import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.officers.Officers

import net.grandcentrix.thirtyinch.TiPresenter

import javax.inject.Inject

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class OfficersPresenter @Inject
constructor(internal var companiesRepository: CompaniesRepository) : TiPresenter<OfficersActivityView>(), Observer<Officers> {

	internal var officers: Officers? = null

	override fun onAttachView(view: OfficersActivityView) {
		super.onAttachView(view)
		officers?.let {
			view.showOfficers(it)
		} ?: run {
			view.showProgress()
			getOfficers()
		}
	}

	@VisibleForTesting
	fun getOfficers() {
		view?.let {
			companiesRepository.getOfficers(it.companyNumber, "0").subscribe(this)
		}
	}

	fun loadMoreOfficers(page: Int) {
		view?.let {
			companiesRepository.getOfficers(it.companyNumber, (page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)).toString()).subscribe(this)
		}
	}

	override fun onComplete() {}

	override fun onSubscribe(d: Disposable) {

	}

	override fun onError(e: Throwable) {
		Log.d("test", "onError: " + e.fillInStackTrace())
		view?.also {
			it.hideProgress()
			it.showError()
		}
	}

	override fun onNext(officers: Officers) {
		this.officers = officers
		view?.also {
			it.hideProgress()
			it.showOfficers(officers)
		}
	}
}
