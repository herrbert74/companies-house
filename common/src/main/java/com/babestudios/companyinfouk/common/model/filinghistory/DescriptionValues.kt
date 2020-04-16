package com.babestudios.companyinfouk.common.model.filinghistory

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DescriptionValues(
		val madeUpDate: String = "",
		val officerName: String = "",
		val appointmentDate: String = "",
		val terminationDate: String = "",
		val newDate: String = "",
		val changeDate: String = "",
		val oldAddress: String = "",
		val newAddress: String = "",
		val formAttached: String = "",
		val chargeNumber: String = "",
		val chargeCreationDate: String = "",
		val date: String = "",
		val capital: List<Capital> = emptyList(),
		val description: String? = null
) : Parcelable
