package com.babestudios.companyinfouk

import com.babestudios.companyinfouk.companies.CompaniesNavigation
import com.babestudios.companyinfouk.navigation.features.CompaniesBaseNavigatable
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

}
