package com.babestudios.base.mvp

import io.reactivex.CompletableSource

interface Presenter<State, out VM : StateViewModel<State>> {
	val viewModel: VM
	var lifeCycleCompletable: CompletableSource
}