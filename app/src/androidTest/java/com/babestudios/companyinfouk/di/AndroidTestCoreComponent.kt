package com.babestudios.companyinfouk.di

import com.babestudios.companyinfouk.core.injection.CoreComponent
import com.babestudios.companyinfouk.navigation.di.NavigationComponent
import com.google.gson.Gson
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidTestDataModule::class], dependencies = [NavigationComponent::class])
interface AndroidTestCoreComponent : CoreComponent {

	fun gson(): Gson

}
