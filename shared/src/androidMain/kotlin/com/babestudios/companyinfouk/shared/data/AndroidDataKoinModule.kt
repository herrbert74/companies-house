package com.babestudios.companyinfouk.shared.data

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.analytics.FirebaseAnalytics
import okhttp3.Interceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidDataModule = module {

	single<Interceptor> {
		ChuckerInterceptor.Builder(androidContext())
			.collector(ChuckerCollector(androidContext(), showNotification = false))
			.build()
	}

	single { FirebaseAnalytics.getInstance(androidContext()) }

}
