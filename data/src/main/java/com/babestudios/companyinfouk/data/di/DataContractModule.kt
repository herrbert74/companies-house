package com.babestudios.companyinfouk.data.di

import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.utils.RawResourceHelper
import com.babestudios.companyinfouk.data.utils.RawResourceHelperContract
import com.babestudios.companyinfouk.data.utils.errors.CompaniesHouseErrorResolver
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Using the [Binds] annotation to bind internal implementations to their interfaces results in less generated code.
 * We also get rid of actually using this module by turning it into an abstract class, then an interface,
 * hence it's separated from [DataModule]
 */
@Module
interface DataContractModule {

	@Singleton
	@Binds
	fun bindCompaniesRepositoryContract(companiesRepository: CompaniesRepository): CompaniesRepositoryContract

	@Singleton
	@Binds
	fun provideRawResourceHelper(rawResourceHelper: RawResourceHelper): RawResourceHelperContract

	@Singleton
	@Binds
	fun provideErrorResolver(companiesHouseErrorResolver: CompaniesHouseErrorResolver): ErrorResolver
}