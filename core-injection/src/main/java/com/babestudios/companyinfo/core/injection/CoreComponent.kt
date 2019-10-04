package com.babestudios.companyinfo.core.injection

import android.content.Context
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.companyinfo.data.CompaniesRepositoryContract
import com.babestudios.companyinfo.data.di.CoreModule
import com.babestudios.companyinfo.navigation.di.NavigationComponent
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [CoreModule::class],
		dependencies = [NavigationComponent::class])
interface CoreComponent {

	fun companiesRepository(): CompaniesRepositoryContract

	fun schedulerProvider(): SchedulerProvider

	fun errorResolver(): ErrorResolver
}