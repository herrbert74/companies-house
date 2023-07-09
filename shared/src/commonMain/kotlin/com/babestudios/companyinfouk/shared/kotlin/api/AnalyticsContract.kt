package com.babestudios.companyinfouk.shared.kotlin.api

/**
 * TODO Use base-kotlin instead
 * This was created because Jitpack cannot publish KMP libraries:
 * https://github.com/jitpack/jitpack.io/issues/3853
 */
interface AnalyticsContract {
	fun logAppOpen()
	fun logScreenView(screenName: String)
	fun logSearch(queryText: String)
}