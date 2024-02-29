package com.babestudios.companyinfouk.shared.ext

import com.arkivanov.decompose.Cancellation
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.rx.observer
import com.arkivanov.mvikotlin.core.store.Store

typealias ValueObserver<T> = (T) -> Unit

fun <T : Any> Store<*, T, *>.asValue(): Value<T> =
	object : Value<T>() {

		override val value: T get() = state

		override fun subscribe(observer: ValueObserver<T>): Cancellation {
			val disposable = states(observer(onNext = observer))
			return Cancellation { disposable.dispose() }
		}

	}
