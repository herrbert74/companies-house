package com.babestudios.companyinfouk.data.di

import android.content.Context
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.companyinfouk.data.model.company.CompanyDto
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.company.Company
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

	fun companiesRepository(): CompaniesRepository

	fun schedulerProvider(): SchedulerProvider

	fun errorResolver(): ErrorResolver

	fun companyMapper() : (CompanyDto) -> Company
}
