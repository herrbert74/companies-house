package com.babestudios.companyinfo.data.model.filinghistory

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList

@Parcelize
class DescriptionValues(
		@SerializedName("made_up_date")
		var madeUpDate: String? = null,

		@SerializedName("officer_name")
		var officerName: String? = null,

		@SerializedName("appointment_date")
		var appointmentDate: String? = null,

		@SerializedName("termination_date")
		var terminationDate: String? = null,

		@SerializedName("new_date")
		var newDate: String? = null,

		@SerializedName("change_date")
		var changeDate: String? = null,

		@SerializedName("old_address")
		var oldAddress: String? = null,

		@SerializedName("new_address")
		var newAddress: String? = null,

		@SerializedName("form_attached")
		var formAttached: String? = null,

		@SerializedName("charge_number")
		var chargeNumber: String? = null,

		@SerializedName("charge_creation_date")
		var chargeCreationDate: String? = null,

		@SerializedName("date")
		var date: String? = null,

		@SerializedName("capital")
		var capital: List<Capital> = ArrayList(),

		var description: String? = null
) : Parcelable
