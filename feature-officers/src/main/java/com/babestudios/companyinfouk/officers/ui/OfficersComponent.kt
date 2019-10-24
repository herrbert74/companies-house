package com.babestudios.companyinfouk.officers.ui

import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.base.di.scope.ActivityScope
import com.babestudios.companyinfouk.core.injection.CoreComponent
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.navigation.features.OfficersNavigator
import dagger.Component

@ActivityScope
@Component(
		dependencies = [CoreComponent::class],
		modules = [OfficersModule::class]
)
interface OfficersComponent {
	fun navigator(): OfficersNavigator
	fun companiesRepository(): CompaniesRepositoryContract

}