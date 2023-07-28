package com.babestudios.companyinfouk.di

import com.babestudios.companyinfouk.mock.mockCompaniesDocumentRepository
import com.babestudios.companyinfouk.mock.mockCompaniesRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

/**
 *
 * Replaces [com.babestudios.companyinfouk.shared.data.androidDataModule].
 *
 * We uninstall the contract module and replace it with mocks from here in order to be
 * able to provide different response for companiesRepository in different tests.
 *
 */
val androidTestDataModule = module {

	single {
		mockCompaniesRepository()
	}.withOptions {
		bind()
	}

	single {
		mockCompaniesDocumentRepository()
	}.withOptions {
		bind()
	}

}