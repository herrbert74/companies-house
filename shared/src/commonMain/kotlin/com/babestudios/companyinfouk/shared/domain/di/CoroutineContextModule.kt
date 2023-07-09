package com.babestudios.companyinfouk.shared.domain.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coroutineContextModule = module {

	single(named("DefaultDispatcher")) {
		Dispatchers.Default
	}.withOptions {
		bind()
	}

	single(named("IoDispatcher")) {
		Dispatchers.IO
	}.withOptions {
		bind()
	}

	single(named("MainDispatcher")) {
		Dispatchers.Main
	}.withOptions {
		bind<CoroutineDispatcher>()
	}

}
