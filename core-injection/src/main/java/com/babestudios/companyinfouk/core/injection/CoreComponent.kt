package com.babestudios.companyinfouk.core.injection

import android.content.Context
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.di.CoreModule
import com.babestudios.companyinfouk.navigation.di.NavigationComponent
import com.babestudios.companyinfouk.navigation.features.ChargesNavigator
import com.babestudios.companyinfouk.navigation.features.FilingsNavigator
import com.babestudios.companyinfouk.navigation.features.OfficersNavigator
import com.babestudios.companyinfouk.navigation.features.PersonsNavigator
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [CoreModule::class],
		dependencies = [NavigationComponent::class])
interface CoreComponent {

	fun companiesRepository(): CompaniesRepositoryContract

	fun schedulerProvider(): SchedulerProvider

	fun errorResolver(): ErrorResolver

	@ApplicationContext
	fun context(): Context

	fun chargesNavigation(): ChargesNavigator

	fun filingsNavigation(): FilingsNavigator

	fun personsNavigation(): PersonsNavigator

	fun officersNavigation(): OfficersNavigator
}