package com.babestudios.companyinfouk.core.injection

import android.content.Context
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.di.DataContractModule
import com.babestudios.companyinfouk.data.di.DataModule
import com.babestudios.companyinfouk.navigation.di.NavigationComponent
import com.babestudios.companyinfouk.navigation.features.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [DataModule::class, DataContractModule::class],
		dependencies = [NavigationComponent::class])
interface CoreComponent {

	@Component.Factory
	interface Factory {
		fun create(
				dataModule: DataModule,
				navigationComponent: NavigationComponent,
				@BindsInstance @ApplicationContext applicationContext: Context
		): CoreComponent
	}

	fun companiesRepository(): CompaniesRepositoryContract

	fun schedulerProvider(): SchedulerProvider

	fun errorResolver(): ErrorResolver

	@ApplicationContext
	fun context(): Context

	fun chargesNavigation(): ChargesNavigator

	fun companiesNavigation(): CompaniesNavigator

	fun filingsNavigation(): FilingsNavigator

	fun insolvenciesNavigation(): InsolvenciesNavigator

	fun personsNavigation(): PersonsNavigator

	fun officersNavigation(): OfficersNavigator
}