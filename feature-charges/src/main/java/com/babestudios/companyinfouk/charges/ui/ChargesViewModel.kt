package com.babestudios.companyinfouk.charges.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.events
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.companyinfouk.charges.ui.charges.ChargesExecutor
import com.babestudios.companyinfouk.charges.ui.charges.ChargesFragment
import com.babestudios.companyinfouk.charges.ui.charges.ChargesStore
import com.babestudios.companyinfouk.charges.ui.charges.ChargesStore.Intent
import com.babestudios.companyinfouk.charges.ui.charges.ChargesStoreFactory
import com.babestudios.companyinfouk.charges.ui.charges.UserIntent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.map

class ChargesViewModel @AssistedInject constructor(
	chargesExecutor: ChargesExecutor,
	@Assisted val companyNumber: String,
) : ViewModel() {

	companion object {

		fun provideFactory(
			assistedFactory: ChargesViewModelFactory,
			companyNumber: String
		): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
			@Suppress("UNCHECKED_CAST")
			override fun <T : ViewModel> create(modelClass: Class<T>): T {
				return assistedFactory.create(companyNumber) as T
			}
		}
	}

	private var chargesStore: ChargesStore =
		ChargesStoreFactory(LoggingStoreFactory(DefaultStoreFactory()), chargesExecutor).create(companyNumber)

	fun onViewCreated(
		view: ChargesFragment,
		lifecycle: Lifecycle
	) {
		bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
			chargesStore.states bindTo view
			chargesStore.labels bindTo { view.sideEffects(it) }
			view.events.map { userIntentToIntent(it) } bindTo chargesStore
		}
	}

	private val userIntentToIntent: UserIntent.() -> Intent =
		{
			when (this) {
				is UserIntent.ChargesItemClicked -> Intent.ChargesItemClicked(selectedChargesItem)
				is UserIntent.LoadMoreCharges -> Intent.LoadMoreCharges(page)
			}
		}

	override fun onCleared() {
		chargesStore::dispose
	}

}

@AssistedFactory
interface ChargesViewModelFactory {
	fun create(companyNumber: String): ChargesViewModel
}
