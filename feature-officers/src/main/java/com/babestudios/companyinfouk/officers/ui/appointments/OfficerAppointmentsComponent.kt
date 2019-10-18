package com.babestudios.companyinfouk.officers.ui.appointments

import com.babestudios.base.di.scope.ActivityScope
import com.babestudios.companyinfouk.core.injection.CoreComponent
import dagger.Component

@ActivityScope
@Component(dependencies = [CoreComponent::class])
interface OfficerAppointmentsComponent {
	fun officerAppointmentsPresenter(): OfficerAppointmentsPresenter
}