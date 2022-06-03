package com.babestudios.companyinfouk.insolvencies.ui

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
import com.babestudios.companyinfouk.domain.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.insolvencies.ui.details.InsolvencyDetailsExecutor
import com.babestudios.companyinfouk.insolvencies.ui.details.InsolvencyDetailsFragment
import com.babestudios.companyinfouk.insolvencies.ui.details.InsolvencyDetailsStore
import com.babestudios.companyinfouk.insolvencies.ui.details.InsolvencyDetailsStoreFactory
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesExecutor
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesFragment
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesStore
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesStore.Intent
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesStoreFactory
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.UserIntent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.map

class InsolvenciesViewModel @AssistedInject constructor(
	insolvenciesExecutor: InsolvenciesExecutor,
	private val insolvencyDetailsExecutor: InsolvencyDetailsExecutor,
	@Assisted val companyNumber: String,
) : ViewModel() {

	companion object {

		fun provideFactory(
			assistedFactory: InsolvenciesViewModelFactory,
			companyNumber: String
		): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
			@Suppress("UNCHECKED_CAST")
			override fun <T : ViewModel> create(modelClass: Class<T>): T {
				return assistedFactory.create(companyNumber) as T
			}
		}
	}

	private var insolvenciesStore: InsolvenciesStore =
		InsolvenciesStoreFactory(LoggingStoreFactory(DefaultStoreFactory()), insolvenciesExecutor).create(companyNumber)

	private var insolvencyDetailsStore: InsolvencyDetailsStore? = null

	fun onViewCreated(
		view: InsolvenciesFragment,
		lifecycle: Lifecycle
	) {
		bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
			insolvenciesStore.states bindTo view
			insolvenciesStore.labels bindTo { view.sideEffects(it) }
			view.events.map { userIntentToIntent(it) } bindTo insolvenciesStore
		}
	}

	fun onViewCreated(
		view: InsolvencyDetailsFragment,
		lifecycle: Lifecycle,
		selectedInsolvencyCase: InsolvencyCase,
	) {
		if(insolvencyDetailsStore == null) {
			insolvencyDetailsStore = InsolvencyDetailsStoreFactory(
			LoggingStoreFactory(DefaultStoreFactory()), insolvencyDetailsExecutor
			).create(companyNumber, selectedInsolvencyCase)
		}
		bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
			insolvencyDetailsStore?.states!! bindTo view
			insolvencyDetailsStore?.labels!! bindTo { view.sideEffects(it) }
			view.events.map { detailsUserIntentToIntent(it) } bindTo insolvencyDetailsStore!!
		}
	}

	private val userIntentToIntent: UserIntent.() -> Intent =
		{
			when (this) {
				is UserIntent.InsolvencyClicked -> Intent.InsolvencyClicked(selectedInsolvency)
			}
		}

	private val detailsUserIntentToIntent: com.babestudios.companyinfouk.insolvencies.ui.details.UserIntent.() ->
	InsolvencyDetailsStore.Intent =
		{
			when (this) {
				is com.babestudios.companyinfouk.insolvencies.ui.details.UserIntent.PractitionerClicked ->
					InsolvencyDetailsStore.Intent.PractitionerClicked(selectedPractitioner)
			}
		}

}

@AssistedFactory
interface InsolvenciesViewModelFactory {
	fun create(companyNumber: String): InsolvenciesViewModel
}
