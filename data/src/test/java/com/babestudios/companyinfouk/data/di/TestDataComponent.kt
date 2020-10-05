package com.babestudios.companyinfouk.data.di

import android.content.Context
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.companyinfouk.common.model.charges.Charges
import com.babestudios.companyinfouk.common.model.company.Company
import com.babestudios.companyinfouk.common.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.common.model.insolvency.Insolvency
import com.babestudios.companyinfouk.common.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.common.model.officers.AppointmentsResponse
import com.babestudios.companyinfouk.common.model.officers.OfficersResponse
import com.babestudios.companyinfouk.common.model.persons.PersonsResponse
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.model.charges.ChargesDto
import com.babestudios.companyinfouk.data.model.company.CompanyDto
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryDto
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyDto
import com.babestudios.companyinfouk.data.model.officers.AppointmentsResponseDto
import com.babestudios.companyinfouk.data.model.officers.OfficersResponseDto
import com.babestudios.companyinfouk.data.model.persons.PersonsResponseDto
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

	fun filingMapper() : (FilingHistoryDto) -> FilingHistory

	fun chargesMapper() : (ChargesDto) -> Charges

	fun insolvencyMapper() : (InsolvencyDto) -> Insolvency

	fun officersMapper() : (OfficersResponseDto) -> OfficersResponse

	fun appointmentsMapper() : (AppointmentsResponseDto) -> AppointmentsResponse

	fun personsMapper() : (PersonsResponseDto) -> PersonsResponse
}