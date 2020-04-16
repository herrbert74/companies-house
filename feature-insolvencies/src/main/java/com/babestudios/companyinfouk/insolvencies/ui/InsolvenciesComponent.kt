package com.babestudios.companyinfouk.insolvencies.ui

import com.babestudios.base.di.scope.ActivityScope
import com.babestudios.companyinfouk.core.injection.CoreComponent
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.navigation.features.InsolvenciesNavigator
import dagger.Component

@ActivityScope
@Component(dependencies = [CoreComponent::class])
interface InsolvenciesComponent {
	fun navigator(): InsolvenciesNavigator
	fun companiesRepository(): CompaniesRepositoryContract
}
