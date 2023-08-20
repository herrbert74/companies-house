package com.babestudios.companyinfouk.shared

import com.babestudios.companyinfouk.shared.di.coroutineContextModule
import com.babestudios.companyinfouk.shared.di.dataModule
import org.koin.core.context.startKoin

fun initKoin(){
	startKoin {
		modules(dataModule, coroutineContextModule)
	}
}
