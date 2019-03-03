package com.babestudios.companyinfouk.ui.officerappointments

import android.os.Parcelable
import com.babestudios.base.mvp.BaseState
import com.babestudios.companyinfouk.ui.officerappointments.list.AbstractOfficerAppointmentsVisitable
import kotlinx.android.parcel.Parcelize

enum class ContentChange {
	NONE,
	APPOINTMENTS_RECEIVED
}

@Parcelize
data class OfficerAppointmentsState(
		var appointmentItems: List<AbstractOfficerAppointmentsVisitable>?,
		var totalResults: Int? = null,
		var officerId: String? = "",
		var officerName : String? = null,
		var contentChange: ContentChange = ContentChange.NONE
) : BaseState(), Parcelable