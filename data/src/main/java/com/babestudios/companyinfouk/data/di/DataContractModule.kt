package com.babestudios.companyinfouk.data.di

import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.companyinfouk.data.CompaniesAccessor
import com.babestudios.companyinfouk.data.CompaniesRxAccessor
import com.babestudios.companyinfouk.data.local.apilookup.ChargesHelperContract
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelperContract
import com.babestudios.companyinfouk.data.local.apilookup.FilingHistoryDescriptionsHelperContract
import com.babestudios.companyinfouk.data.local.apilookup.PscHelperContract
import com.babestudios.companyinfouk.data.mappers.CompaniesHouseMapping
import com.babestudios.companyinfouk.data.utils.RawResourceHelper
import com.babestudios.companyinfouk.data.utils.RawResourceHelperContract
import com.babestudios.companyinfouk.data.utils.StringResourceHelperContract
import com.babestudios.companyinfouk.data.utils.errors.CompaniesHouseRxErrorResolver
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.api.CompaniesRxRepository
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
	fun bindCompaniesRepository(companiesRepository: CompaniesAccessor): CompaniesRepository

	@Singleton
	@Binds
	fun bindCompaniesRxRepository(companiesRepository: CompaniesRxAccessor): CompaniesRxRepository

	@Singleton
	@Binds
	fun provideRawResourceHelper(rawResourceHelper: RawResourceHelper): RawResourceHelperContract

	@Singleton
	@Binds
	fun provideErrorResolver(companiesHouseErrorResolver: CompaniesHouseRxErrorResolver): ErrorResolver

}
