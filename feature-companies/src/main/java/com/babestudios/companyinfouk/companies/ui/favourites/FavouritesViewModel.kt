package com.babestudios.companyinfouk.companies.ui.favourites

import androidx.lifecycle.ViewModel
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.events
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesStore.Intent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.map

@HiltViewModel
class FavouritesViewModel @Inject constructor(favouritesExecutor: FavouritesExecutor) : ViewModel() {

	private var favouritesStore: FavouritesStore =
		FavouritesStoreFactory(LoggingStoreFactory(DefaultStoreFactory()), favouritesExecutor).create()

	fun onViewCreated(
		view: FavouritesFragment,
		lifecycle: Lifecycle
	) {
		bind(lifecycle, BinderLifecycleMode.RESUME_PAUSE) {
			favouritesStore.states bindTo view
			favouritesStore.labels bindTo { view.sideEffects(it) }
			view.events.map { userIntentToIntent(it) } bindTo favouritesStore
		}
	}

	private val userIntentToIntent: UserIntent.() -> Intent =
		{
			when (this) {
				is UserIntent.FavouritesItemClicked -> Intent.FavouritesItemClicked(favouritesListItem)
				is UserIntent.CancelPendingRemoval -> Intent.CancelPendingRemoval(favouritesListItem)
				is UserIntent.InitPendingRemoval -> Intent.InitPendingRemoval(favouritesListItem)
				is UserIntent.RemoveItem -> Intent.RemoveItem(favouritesListItem)
				is UserIntent.DeletedInCompany -> Intent.DeletedInCompany
			}
		}

	override fun onCleared() {
		favouritesStore::dispose
	}

}
