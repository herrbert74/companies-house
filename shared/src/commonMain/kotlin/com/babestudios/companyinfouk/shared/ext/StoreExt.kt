package com.babestudios.companyinfouk.shared.ext

import com.arkivanov.decompose.Cancellation
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.rx.Disposable
import com.arkivanov.mvikotlin.core.rx.observer

typealias ValueObserver<T> = (T) -> Unit

fun <T : Any> Store<*, T, *>.asValue(): Value<T> =
	object : Value<T>() {

		private var disposables = emptyMap<ValueObserver<T>, Disposable>()

		override val value: T get() = state

		override fun subscribe(observer: ValueObserver<T>): Cancellation {
			val disposable = states(observer(onNext = observer))
			this.disposables += observer to disposable
			return Cancellation { disposable.dispose() }
		}

	}
