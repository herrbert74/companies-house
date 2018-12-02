package com.babestudios.companyinfouk

import com.babestudios.companyinfouk.injection.ApplicationComponent
import com.babestudios.companyinfouk.ui.search.SearchPresenter

import javax.inject.Singleton

import dagger.Component

@Singleton
@Component(modules = [TestApplicationModule::class])
interface TestApplicationComponent : ApplicationComponent {

	override fun inject(searchPresenter: SearchPresenter)

}
