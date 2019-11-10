package com.babestudios.companyinfouk.navigation.di

import com.babestudios.companyinfouk.navigation.features.*

/**
 * This is not a real Dagger Component, only an interface to break circular dependency with navigation.
 * See [Dagger Component Dependencies for Library Development](https://proandroiddev.com/dagger-component-dependencies-for-library-development-e2df7ce68233)
 * and [Component dependencies](https://dagger.dev/api/2.14/dagger/Component.html#dependencies--)
 */
interface NavigationComponent {
	fun provideChargesNavigation(): ChargesNavigator
	fun provideCompaniesNavigation(): CompaniesNavigator
	fun provideInsolvenciesNavigation(): InsolvenciesNavigator
	fun provideFilingsNavigation(): FilingsNavigator
	fun provideOfficersNavigation(): OfficersNavigator
	fun providePersonsNavigation(): PersonsNavigator
}