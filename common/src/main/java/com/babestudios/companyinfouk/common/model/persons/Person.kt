package com.babestudios.companyinfouk.common.model.persons


import android.os.Parcelable
import androidx.annotation.Keep
import com.babestudios.companyinfouk.common.model.common.Address
import com.babestudios.companyinfouk.common.model.common.MonthYear
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Person(
		var notifiedOn: String,
		var ceasedOn: String? = null,
		var kind: String,
		var countryOfResidence: String? = null,
		var dateOfBirth: MonthYear? = null,
		var address: Address,
		var naturesOfControl: List<String>,
		var nationality: String? = null,
		@Keep var name: String? = null,
		var identification: Identification? = null,
) : Parcelable

@Parcelize
data class Identification(
		var countryRegistered: String? = null,
		var legalAuthority: String,
		var legalForm: String,
		var placeRegistered: String? = null,
		var registrationNumber: String? = null
) : Parcelable
