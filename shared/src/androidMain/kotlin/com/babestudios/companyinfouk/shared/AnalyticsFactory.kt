package com.babestudios.companyinfouk.shared

import android.os.Bundle
import com.babestudios.base.kotlin.api.AnalyticsContract
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class AnalyticsFactory : KoinComponent {

	actual fun createAnalytics(): AnalyticsContract = object : AnalyticsContract {

		val firebaseAnalytics: FirebaseAnalytics by inject()

		override fun logAppOpen() {
			firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null)
		}

		override fun logScreenView(screenName: String) {
			val bundle = Bundle()
			bundle.putString("screen_name", screenName)
			firebaseAnalytics.logEvent("screenView", bundle)
		}

		override fun logSearch(queryText: String) {
			val bundle = Bundle()
			bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, queryText)
			firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle)
		}
	}

}