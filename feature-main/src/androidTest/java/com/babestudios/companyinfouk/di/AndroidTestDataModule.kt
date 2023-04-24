package com.babestudios.companyinfouk.di

import com.babestudios.companyinfouk.mock.mockCompaniesRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

val androidTestDataModule = module {
	single {
		mockCompaniesRepository()
	}.withOptions {
		bind()
	}
}