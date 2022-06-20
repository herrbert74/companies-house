package com.babestudios.companyinfouk.data.di

import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.companyinfouk.data.CompaniesAccessor
import com.babestudios.companyinfouk.data.utils.RawResourceHelper
import com.babestudios.companyinfouk.data.utils.RawResourceHelperContract
import com.babestudios.companyinfouk.data.utils.errors.CompaniesHouseRxErrorResolver
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Using the [Binds] annotation to bind internal implementations to their interfaces results in less generated code.
 * We also get rid of actually using this module by turning it into an abstract class, then an interface,
 * hence it's separated from [DataModule]
 */
@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
interface DataContractModule {

	@Singleton
	@Binds
	fun bindCompaniesRepository(companiesRepository: CompaniesAccessor): CompaniesRepository

	@Singleton
	@Binds
	fun provideRawResourceHelper(rawResourceHelper: RawResourceHelper): RawResourceHelperContract

	@Singleton
	@Binds
	fun provideErrorResolver(companiesHouseErrorResolver: CompaniesHouseRxErrorResolver): ErrorResolver

}
