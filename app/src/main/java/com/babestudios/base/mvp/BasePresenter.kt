package com.babestudios.base.mvp

import android.annotation.SuppressLint
import com.babestudios.companyinfouk.ui.filinghistory.FilingHistoryViewModel
import io.reactivex.CompletableSource
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.zipWith

abstract class BasePresenter<S : BaseState, VM : StateViewModel<S>> : Presenter<S, VM> {

	var viewModel: VM? = null
	var lifeCycleCompletable: CompletableSource? = null

	@SuppressLint("CheckResult")
	fun sendToViewModel(reducer: (S) -> S) {
		viewModel?.apply {
			Observable.just(reducer)
					.observeOn(AndroidSchedulers.mainThread())
					.zipWith(this.state)
					.map { (reducer, state) -> reducer.invoke(state) }
					.subscribe(this.state)
		}
	}

	open fun onError(errorType: ErrorType, errorMessage: String?) {
		sendToViewModel { it ->
			it.apply {
				this.isLoading = false
				this.errorType = errorType
				this.errorMessage = errorMessage
			}
		}

	}
}