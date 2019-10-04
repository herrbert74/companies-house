package com.babestudios.companyinfouk.ui.officerdetails

import com.babestudios.base.di.scope.ActivityScope
import com.babestudios.companyinfo.core.injection.CoreComponent
import dagger.Component

@ActivityScope
@Component(dependencies = [CoreComponent::class])
interface OfficerDetailsComponent {
	fun officerDetailsPresenter(): OfficerDetailsPresenter
}