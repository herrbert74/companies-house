package com.babestudios.companyinfouk

import com.babestudios.companyinfouk.injection.ApplicationComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TestApplicationModule::class])
interface TestApplicationComponent : ApplicationComponent {


}
