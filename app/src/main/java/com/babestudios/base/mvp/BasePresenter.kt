package com.babestudios.base.mvp

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.zipWith

abstract class BasePresenter<S : BaseState, VM : StateViewModel<S>>(override var viewModel: VM) : Presenter<S, VM> {

	@SuppressLint("CheckResult")
	fun sendToViewModel(reducer: (S) -> S) {
		Observable.just(reducer)
				.observeOn(AndroidSchedulers.mainThread())
				.zipWith(viewModel.state)
				.map { (reducer, state) -> reducer.invoke(state) }
				.subscribe(viewModel.state)
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