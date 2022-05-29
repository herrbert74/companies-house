package com.babestudios.companyinfouk

import com.babestudios.companyinfouk.companies.CompaniesNavigation
import com.babestudios.companyinfouk.filings.FilingsNavigation
import com.babestudios.companyinfouk.insolvencies.InsolvenciesNavigation
import com.babestudios.companyinfouk.navigation.features.CompaniesBaseNavigatable
import com.babestudios.companyinfouk.navigation.features.FilingsBaseNavigatable
import com.babestudios.companyinfouk.navigation.features.InsolvenciesBaseNavigatable
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

}
