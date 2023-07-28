package com.babestudios.companyinfouk.shared

import com.babestudios.base.kotlin.api.AnalyticsContract

expect class AnalyticsFactory() {
	fun createAnalytics(): AnalyticsContract
}