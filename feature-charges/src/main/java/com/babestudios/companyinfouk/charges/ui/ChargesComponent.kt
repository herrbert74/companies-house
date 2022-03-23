package com.babestudios.companyinfouk.charges.ui

import com.babestudios.base.di.scope.ActivityScope
import com.babestudios.companyinfouk.core.injection.CoreComponent
import com.babestudios.companyinfouk.domain.api.CompaniesRxRepository
import com.babestudios.companyinfouk.navigation.features.ChargesNavigator
import dagger.Component

@ActivityScope
@Component(dependencies = [CoreComponent::class])
interface ChargesComponent {
	fun navigator(): ChargesNavigator
	fun companiesRepository(): CompaniesRxRepository
}
