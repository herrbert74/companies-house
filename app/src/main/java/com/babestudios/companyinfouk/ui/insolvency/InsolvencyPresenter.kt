package com.babestudios.companyinfouk.ui.insolvency

import androidx.annotation.VisibleForTesting
import android.util.Log
import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.insolvency.Insolvency
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import net.grandcentrix.thirtyinch.TiPresenter
import retrofit2.HttpException
import javax.inject.Inject

class InsolvencyPresenter @Inject
constructor(internal var companiesRepository: CompaniesRepository) : TiPresenter<InsolvencyActivityView>(), Observer<Insolvency> {

	internal var insolvency: Insolvency? = null

	override fun onAttachView(view: InsolvencyActivityView) {
		super.onAttachView(view)
		if (insolvency != null) {
			showInsolvency(insolvency)
		} else {
			view.showProgress()
			getInsolvency()
		}
	}

	@VisibleForTesting
	fun getInsolvency() {
		view?.let {
			companiesRepository.getInsolvency(it.companyNumber).subscribe(this)
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
				view?.showNoInsolvency()
			} else {
				view?.showError()
				view?.hideProgress()
			}
		} else {
			view?.showError()
			view?.hideProgress()
		}
	}

	override fun onNext(insolvency: Insolvency) {
		this.insolvency = insolvency
		view?.hideProgress()
		showInsolvency(insolvency)

	}

	private fun showInsolvency(insolvency: Insolvency?) {
		if (insolvency != null) {
			view?.showInsolvency(insolvency)
		} else {
			view?.showNoInsolvency()
		}
	}
}
