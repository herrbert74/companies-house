package com.babestudios.companyinfouk.ui.officers

import com.babestudios.base.di.scope.ActivityScope
import com.babestudios.companyinfouk.core.injection.CoreComponent
import dagger.Component

@ActivityScope
@Component(dependencies = [CoreComponent::class])
interface OfficersComponent {
	fun officersPresenter(): OfficersPresenter
}