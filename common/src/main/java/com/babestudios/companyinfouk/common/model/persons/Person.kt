package com.babestudios.companyinfouk.common.model.persons


import android.os.Parcelable
import androidx.annotation.Keep
import com.babestudios.companyinfouk.common.model.common.Address
import com.babestudios.companyinfouk.common.model.common.MonthYear
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Person(
		val notifiedOn: String,
		val ceasedOn: String? = null,
		val kind: String,
		val countryOfResidence: String? = null,
		val dateOfBirth: MonthYear? = null,
		val address: Address,
		val naturesOfControl: List<String>,
		val nationality: String? = null,
		@Keep val name: String? = null,
		val identification: Identification? = null,
) : Parcelable

@Parcelize
data class Identification(
		val countryRegistered: String? = null,
		val legalAuthority: String,
		val legalForm: String,
		val placeRegistered: String? = null,
		val registrationNumber: String? = null
) : Parcelable
