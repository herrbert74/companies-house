package com.babestudios.companyinfouk.persons.ui

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
import com.babestudios.companyinfouk.persons.ui.persons.PersonsExecutor
import com.babestudios.companyinfouk.persons.ui.persons.PersonsFragment
import com.babestudios.companyinfouk.persons.ui.persons.PersonsStore
import com.babestudios.companyinfouk.persons.ui.persons.PersonsStore.Intent
import com.babestudios.companyinfouk.persons.ui.persons.PersonsStoreFactory
import com.babestudios.companyinfouk.persons.ui.persons.UserIntent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.map

class PersonsViewModel @AssistedInject constructor(
	personsExecutor: PersonsExecutor,
	@Assisted val companyNumber: String,
) : ViewModel() {

	companion object {

		fun provideFactory(
			assistedFactory: PersonsViewModelFactory,
			companyNumber: String
		): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
			@Suppress("UNCHECKED_CAST")
			override fun <T : ViewModel> create(modelClass: Class<T>): T {
				return assistedFactory.create(companyNumber) as T
			}
		}
	}

	private var personsStore: PersonsStore =
		PersonsStoreFactory(LoggingStoreFactory(DefaultStoreFactory()), personsExecutor).create(companyNumber)

	fun onViewCreated(
		view: PersonsFragment,
		lifecycle: Lifecycle
	) {
		bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
			personsStore.states bindTo view
			personsStore.labels bindTo { view.sideEffects(it) }
			view.events.map { userIntentToIntent(it) } bindTo personsStore
		}
	}

	private val userIntentToIntent: UserIntent.() -> Intent =
		{
			when (this) {
				is UserIntent.PersonsItemClicked -> Intent.PersonsItemClicked(selectedPerson)
				is UserIntent.LoadMorePersons -> Intent.LoadMorePersons(page)
			}
		}

}

@AssistedFactory
interface PersonsViewModelFactory {
	fun create(companyNumber: String): PersonsViewModel
}
