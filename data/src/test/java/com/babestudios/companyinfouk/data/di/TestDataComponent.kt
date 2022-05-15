package com.babestudios.companyinfouk.data.di

import android.content.Context
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.companyinfouk.data.mappers.CompaniesHouseMapping
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.api.CompaniesRxRepository
import com.babestudios.companyinfouk.domain.util.CoroutineContextModule
import dagger.BindsInstance
import dagger.Component
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Singleton
@Component(
	modules = [
		TestDataModule::class,
		TestDataContractModule::class,
		MapperModule::class,
		CoroutineContextModule::class
	]
)
interface TestDataComponent {

	@Component.Factory
	interface Factory {
		fun create(
			testDataModule: TestDataModule,
			mapperModule: MapperModule,
			coroutineContextModule: CoroutineContextModule,
			@BindsInstance @ApplicationContext applicationContext: Context
		): TestDataComponent
	}

	fun companiesRxRepository(): CompaniesRxRepository

	fun companiesRepository(): CompaniesRepository

	fun schedulerProvider(): SchedulerProvider

	fun errorResolver(): ErrorResolver

	fun companiesHouseMapping(): CompaniesHouseMapping

}