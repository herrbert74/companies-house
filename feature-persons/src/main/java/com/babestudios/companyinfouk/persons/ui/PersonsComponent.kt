package com.babestudios.companyinfouk.persons.ui

import com.babestudios.base.di.scope.ActivityScope
import com.babestudios.companyinfouk.core.injection.CoreComponent
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.navigation.features.PersonsNavigator
import dagger.Component

@ActivityScope
@Component(dependencies = [CoreComponent::class])
interface PersonsComponent {
	fun navigator(): PersonsNavigator
	fun companiesRepository(): CompaniesRepository
}
