package com.babestudios.companyinfouk.companies.ui.company

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
import com.babestudios.companyinfouk.companies.ui.company.CompanyStore.Intent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.map

class CompanyViewModel @AssistedInject constructor(
	companyExecutor: CompanyExecutor,
	@Assisted val companyNumber: String,
) : ViewModel() {

	companion object {

		fun provideFactory(
			assistedFactory: CompanyViewModelFactory,
			companyNumber: String
		): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
			@Suppress("UNCHECKED_CAST")
			override fun <T : ViewModel> create(modelClass: Class<T>): T {
				return assistedFactory.create(companyNumber) as T
			}
		}
	}

	private var companyStore: CompanyStore =
		CompanyStoreFactory(LoggingStoreFactory(DefaultStoreFactory()), companyExecutor).create(companyNumber)

	fun onViewCreated(
		view: CompanyFragment,
		lifecycle: Lifecycle
	) {
		bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
			companyStore.states bindTo view
			companyStore.labels bindTo { view.sideEffects(it) }
			view.events.map { userIntentToIntent(it) } bindTo companyStore
		}
	}

	private val userIntentToIntent: UserIntent.() -> Intent =
		{
			when (this) {
				is UserIntent.FabFavouritesClicked -> Intent.FabFavouritesClicked
				is UserIntent.MapClicked -> Intent.MapClicked
			}
		}

}

@AssistedFactory
interface CompanyViewModelFactory {
	fun create(companyNumber: String): CompanyViewModel
}
