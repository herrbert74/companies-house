package com.babestudios.companyinfouk.data.di

import com.babestudios.companyinfouk.data.local.apilookup.ChargesHelperContract
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelperContract
import com.babestudios.companyinfouk.data.local.apilookup.FilingHistoryDescriptionsHelperContract
import com.babestudios.companyinfouk.data.local.apilookup.PscHelperContract
import com.babestudios.companyinfouk.data.mappers.CompaniesHouseMapper
import com.babestudios.companyinfouk.data.mappers.CompaniesHouseMapping
import com.babestudios.companyinfouk.data.utils.StringResourceHelperContract
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
@Suppress("unused")
class MapperModule {

	@Provides
	@Singleton
	fun provideCompaniesHouseMapping(
		filingHistoryDescriptionsHelper: FilingHistoryDescriptionsHelperContract,
		chargesHelper: ChargesHelperContract,
		constantsHelper: ConstantsHelperContract,
		stringResourceHelper: StringResourceHelperContract,
		pscHelper: PscHelperContract,
	): CompaniesHouseMapping {
		return CompaniesHouseMapper(
			filingHistoryDescriptionsHelper, chargesHelper, constantsHelper, stringResourceHelper, pscHelper
		)
	}

}
