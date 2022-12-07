package com.babestudios.companyinfouk.domain.model.officers


import android.os.Parcelable
import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.domain.model.common.MonthYear
import kotlinx.parcelize.Parcelize

data class AppointmentsResponse(
	var dateOfBirth: MonthYear? = null,
	var items: List<Appointment> = emptyList(),
	var name: String = "",
	var totalResults: Int = 0,
)

@Parcelize
data class Appointment(
	var address: Address = Address(),
	var appointedOn: String? = null,
	var appointedTo: AppointedTo = AppointedTo(),
	var countryOfResidence: String? = null,
	var name: String = "",
	var nationality: String? = null,
	var occupation: String? = null,
	var officerRole: String = "",
	var resignedOn: String? = null
) : Parcelable

@Parcelize
class AppointedTo(
		var companyName: String = "",
		var companyNumber: String = "",
		var companyStatus: String = "",
) : Parcelable
