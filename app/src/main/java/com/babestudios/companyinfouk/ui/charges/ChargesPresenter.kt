package com.babestudios.companyinfouk.ui.charges

import android.support.annotation.VisibleForTesting
import android.util.Log

import com.babestudios.companyinfouk.BuildConfig
import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.charges.Charges

import net.grandcentrix.thirtyinch.TiPresenter

import javax.inject.Inject

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException

class ChargesPresenter @Inject
constructor(var companiesRepository: CompaniesRepository) : TiPresenter<ChargesActivityView>(), Observer<Charges> {

	override fun onAttachView(view: ChargesActivityView) {
		super.onAttachView(view)
		view.showProgress()
		getCharges()
	}

	@VisibleForTesting
	fun getCharges() {
		companiesRepository.getCharges(view?.companyNumber ?: "", "0").subscribe(this)
	}

	fun loadMoreCharges(page: Int) {
		if (charges == null || charges!!.items.size < totalCount?.toInt()!!) {
			companiesRepository.getCharges(view?.companyNumber
					?: "", (page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)).toString()).subscribe(this)
		}
	}

	override fun onComplete() {}

	override fun onSubscribe(d: Disposable) {

	}

	override fun onError(e: Throwable) {
		view?.hideProgress()
		Log.d("test", "onError: " + e.fillInStackTrace())
		if (e is HttpException) {
			if (e.code() == 404) {
				view?.showNoCharges()
			} else {
				view?.showError()
			}
		} else {
			view?.showError()
		}
	}

	private var totalCount: String? = null

	private var charges: Charges? = null

	override fun onNext(charges: Charges) {
		totalCount = charges.totalCount
		this.charges = charges
		view?.hideProgress()
		view?.showCharges(charges)
	}

}
