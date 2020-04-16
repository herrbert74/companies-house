package com.babestudios.companyinfouk.companies.ui

import com.babestudios.base.di.scope.ActivityScope
import com.babestudios.companyinfouk.core.injection.CoreComponent
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.navigation.features.CompaniesNavigator
import dagger.Component

@ActivityScope
@Component(dependencies = [CoreComponent::class])
interface CompaniesComponent {
	fun navigator(): CompaniesNavigator
	fun companiesRepository(): CompaniesRepositoryContract
}
