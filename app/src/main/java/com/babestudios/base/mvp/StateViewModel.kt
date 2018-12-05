package com.babestudios.base.mvp

import android.arch.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay

abstract class StateViewModel<S>(initialState: S) : ViewModel() {
	var state = BehaviorRelay.createDefault(initialState)!!
}