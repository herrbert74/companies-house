package com.babestudios.companyinfouk.filings.ui

import com.babestudios.base.di.scope.ActivityScope
import com.babestudios.companyinfouk.core.injection.CoreComponent
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.navigation.features.FilingsNavigator
import dagger.Component

@ActivityScope
@Component(dependencies = [CoreComponent::class])
interface FilingsComponent {
	fun navigator(): FilingsNavigator
	fun companiesRepository(): CompaniesRepository
}
