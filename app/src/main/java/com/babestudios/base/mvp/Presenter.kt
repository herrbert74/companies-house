package com.babestudios.base.mvp

interface Presenter<State, out VM : StateViewModel<State>> {
	val viewModel: VM
}