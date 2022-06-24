package com.babestudios.companyinfouk.filings.ui

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
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
import com.babestudios.companyinfouk.domain.model.filinghistory.Category
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.filings.ui.details.FilingHistoryDetailsExecutor
import com.babestudios.companyinfouk.filings.ui.details.FilingHistoryDetailsFragment
import com.babestudios.companyinfouk.filings.ui.details.FilingHistoryDetailsStore
import com.babestudios.companyinfouk.filings.ui.details.FilingHistoryDetailsStoreFactory
import com.babestudios.companyinfouk.filings.ui.filinghistory.FilingHistoryExecutor
import com.babestudios.companyinfouk.filings.ui.filinghistory.FilingHistoryFragment
import com.babestudios.companyinfouk.filings.ui.filinghistory.FilingHistoryStore
import com.babestudios.companyinfouk.filings.ui.filinghistory.FilingHistoryStore.Intent
import com.babestudios.companyinfouk.filings.ui.filinghistory.FilingsHistoryStoreFactory
import com.babestudios.companyinfouk.filings.ui.filinghistory.UserIntent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.map

class FilingsViewModel @AssistedInject constructor(
	filingHistoryExecutor: FilingHistoryExecutor,
	private val filingHistoryDetailsExecutor: FilingHistoryDetailsExecutor,
	@Assisted val companyNumber: String,
) : ViewModel() {

	companion object {

		fun provideFactory(
			assistedFactory: FilingsViewModelFactory,
			companyNumber: String
		): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
			@Suppress("UNCHECKED_CAST")
			override fun <T : ViewModel> create(modelClass: Class<T>): T {
				return assistedFactory.create(companyNumber) as T
			}
		}
	}

	private var filingHistoryStore: FilingHistoryStore = FilingsHistoryStoreFactory(
		LoggingStoreFactory(DefaultStoreFactory()),
		filingHistoryExecutor
	).create(companyNumber)

	private var filingHistoryDetailsStore: FilingHistoryDetailsStore? = null

	fun onViewCreated(
		view: FilingHistoryFragment,
		lifecycle: Lifecycle
	) {
		bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
			filingHistoryStore.states bindTo view
			filingHistoryStore.labels bindTo { view.sideEffects(it) }
			view.events.map { userIntentToIntent(it) } bindTo filingHistoryStore
		}
	}

	fun onViewCreated(
		view: FilingHistoryDetailsFragment,
		lifecycle: Lifecycle,
		selectedFilingHistoryItem: FilingHistoryItem,
	) {
		if (filingHistoryDetailsStore == null) {
			filingHistoryDetailsStore = FilingHistoryDetailsStoreFactory(
				LoggingStoreFactory(DefaultStoreFactory()), filingHistoryDetailsExecutor
			).create(companyNumber, selectedFilingHistoryItem)
		}
		bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
			filingHistoryDetailsStore!!.states bindTo view
			view.events.map { detailsUserIntentToIntent(it) } bindTo filingHistoryDetailsStore!!
		}
	}

	private val userIntentToIntent: UserIntent.() -> Intent =
		{
			when (this) {
				is UserIntent.FilingItemClicked -> Intent.FilingHistoryItemClicked(selectedFilingHistoryItem)
				is UserIntent.LoadMoreFilings -> Intent.LoadMoreFilingHistory(page)
				is UserIntent.FilingCategorySelected -> Intent.FilterClicked(Category.values()[categoryOrdinal])
			}
		}

	private val detailsUserIntentToIntent: com.babestudios.companyinfouk.filings.ui.details.UserIntent.() ->
	FilingHistoryDetailsStore.Intent =
		{
			when (this) {
				is com.babestudios.companyinfouk.filings.ui.details.UserIntent.FetchDocument ->
					FilingHistoryDetailsStore.Intent.FetchDocument(documentId)
				is com.babestudios.companyinfouk.filings.ui.details.UserIntent.WriteDocument ->
					FilingHistoryDetailsStore.Intent.WriteDocument(uri)
			}
		}

	override fun onCleared() {
		filingHistoryStore::dispose
		filingHistoryDetailsStore?.dispose()
	}

	//endregion
}

fun String?.createSpannableDescription(): Spannable? {
	var s = this
	if (s != null) {
		val firstOpen = s.indexOf("**")
		s = s.replaceFirst("**", "")
		val firstClose = s.indexOf("**", firstOpen + 1)
		s = s.replaceFirst("**", "")
		val secondOpen = s.indexOf("**")
		s = s.replaceFirst("**", "")
		val secondClose = s.indexOf("**", firstOpen + 1)
		s = s.replaceFirst("**", "")
		val spannable = SpannableString(s)
		if (firstOpen > -1) {
			val boldSpan = StyleSpan(Typeface.BOLD)
			spannable.setSpan(boldSpan, firstOpen, firstClose, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
			if (secondOpen > -1) {
				spannable.setSpan(boldSpan, secondOpen, secondClose, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
			}
		}
		return spannable
	}
	return null
}

@AssistedFactory
interface FilingsViewModelFactory {
	fun create(companyNumber: String): FilingsViewModel
}
