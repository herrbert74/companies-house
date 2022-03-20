package com.babestudios.companyinfouk.core.injection

import android.content.Context
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.base.di.scope.ApplicationScope
import com.babestudios.companyinfouk.data.di.DataComponent
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.navigation.di.NavigationComponent
import com.babestudios.companyinfouk.navigation.features.ChargesNavigator
import com.babestudios.companyinfouk.navigation.features.CompaniesNavigator
import com.babestudios.companyinfouk.navigation.features.FilingsNavigator
import com.babestudios.companyinfouk.navigation.features.InsolvenciesNavigator
import com.babestudios.companyinfouk.navigation.features.OfficersNavigator
import com.babestudios.companyinfouk.navigation.features.PersonsNavigator
import dagger.BindsInstance
import dagger.Component


@ApplicationScope
@Component(dependencies = [DataComponent::class, NavigationComponent::class])
interface CoreComponent {

	@Component.Factory
	interface Factory {
		fun create(
				dataComponent: DataComponent,
				navigationComponent: NavigationComponent,
				@BindsInstance @ApplicationContext applicationContext: Context,
		): CoreComponent
	}

	fun companiesRepository(): CompaniesRepository

	@ApplicationContext
	fun context(): Context

	fun chargesNavigation(): ChargesNavigator

	fun companiesNavigation(): CompaniesNavigator

	fun filingsNavigation(): FilingsNavigator

	fun insolvenciesNavigation(): InsolvenciesNavigator

	fun personsNavigation(): PersonsNavigator

	fun officersNavigation(): OfficersNavigator
}
