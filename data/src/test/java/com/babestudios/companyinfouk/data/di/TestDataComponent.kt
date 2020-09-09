package com.babestudios.companyinfouk.data.di

import android.content.Context
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.companyinfouk.common.model.company.Company
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.model.company.CompanyDto
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TestDataModule::class, TestDataContractModule::class])
interface TestDataComponent {

	@Component.Factory
	interface Factory {
		fun create(
				testDataModule: TestDataModule,
				@BindsInstance @ApplicationContext applicationContext: Context
		): TestDataComponent
	}

	fun companiesRepository(): CompaniesRepositoryContract

	fun schedulerProvider(): SchedulerProvider

	fun errorResolver(): ErrorResolver

	fun companyMapper() : (CompanyDto) -> Company
}