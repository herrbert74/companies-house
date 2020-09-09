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
@Component(modules = [DataModule::class, DataContractModule::class])
interface DataComponent {

	@Component.Factory
	interface Factory {
		fun create(
				dataModule: DataModule,
				@BindsInstance @ApplicationContext applicationContext: Context
		): DataComponent
	}

	fun companiesRepository(): CompaniesRepositoryContract

	fun schedulerProvider(): SchedulerProvider

	fun errorResolver(): ErrorResolver

	fun companyMapper() : (CompanyDto) -> Company
}
