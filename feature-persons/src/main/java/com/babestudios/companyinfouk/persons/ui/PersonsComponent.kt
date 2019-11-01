package com.babestudios.companyinfouk.persons.ui

import com.babestudios.base.di.scope.ActivityScope
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.companyinfouk.core.injection.CoreComponent
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.navigation.features.PersonsNavigator
import dagger.Component

@ActivityScope
@Component(dependencies = [CoreComponent::class])
interface PersonsComponent {
	fun navigator(): PersonsNavigator
	fun companiesRepository(): CompaniesRepositoryContract
	fun errorResolver(): ErrorResolver
}