package com.babestudios.companyinfouk.shared.data

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidDataModule = module {
	single {
		ChuckerInterceptor.Builder(androidContext())
			.collector(ChuckerCollector(androidContext(), showNotification = false))
			.build()
	}
}