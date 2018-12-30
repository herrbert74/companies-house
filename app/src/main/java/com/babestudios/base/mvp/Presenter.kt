package com.babestudios.base.mvp

import io.reactivex.CompletableSource

interface Presenter<State, VM : StateViewModel<State>> {
	fun setViewModel(viewModel: VM?, lifeCycleCompletable: CompletableSource?)
}