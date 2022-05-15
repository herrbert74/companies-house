package com.babestudios.companyinfouk

import com.babestudios.companyinfouk.charges.ChargesNavigation
import com.babestudios.companyinfouk.companies.CompaniesNavigation
import com.babestudios.companyinfouk.filings.FilingsNavigation
import com.babestudios.companyinfouk.insolvencies.InsolvenciesNavigation
import com.babestudios.companyinfouk.navigation.features.ChargesBaseNavigatable
import com.babestudios.companyinfouk.navigation.features.CompaniesBaseNavigatable
import com.babestudios.companyinfouk.navigation.features.FilingsBaseNavigatable
import com.babestudios.companyinfouk.navigation.features.InsolvenciesBaseNavigatable
import com.babestudios.companyinfouk.navigation.features.OfficersBaseNavigatable
import com.babestudios.companyinfouk.navigation.features.PersonsBaseNavigatable
import com.babestudios.companyinfouk.officers.OfficersNavigation
import com.babestudios.companyinfouk.persons.PersonsNavigation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@Suppress("unused")
@InstallIn(SingletonComponent::class)
object NavigationModule {

	@Singleton
	@Provides
	fun provideChargesNavigatable(): ChargesBaseNavigatable {
		return ChargesNavigation()
	}

	@Singleton
	@Provides
	fun provideCompaniesNavigatable(): CompaniesBaseNavigatable {
		return CompaniesNavigation()
	}

	@Singleton
	@Provides
	fun provideFilingsNavigatable(): FilingsBaseNavigatable{
		return FilingsNavigation()
	}

	@Singleton
	@Provides
	fun provideInsolvenciesNavigatable( ): InsolvenciesBaseNavigatable{
		return InsolvenciesNavigation()
	}

	@Singleton
	@Provides
	fun provideOfficersNavigatable(): OfficersBaseNavigatable{
		return OfficersNavigation()
	}

	@Singleton
	@Provides
	fun providePersonsNavigatable(): PersonsBaseNavigatable{
		return PersonsNavigation()
	}

}
