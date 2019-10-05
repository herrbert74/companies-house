package com.babestudios.companyinfouk.ui.charges

import com.babestudios.base.di.scope.ActivityScope
import com.babestudios.companyinfouk.core.injection.CoreComponent
import dagger.Component

@ActivityScope
@Component(dependencies = [CoreComponent::class])
interface ChargesComponent {
	fun chargesPresenter(): ChargesPresenter
}