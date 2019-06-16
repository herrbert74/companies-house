package com.babestudios.companyinfouk.data.model.company

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CompanyLinks(

		@SerializedName("charges")
		var charges: String? = null,

		@SerializedName("filing_history")
		var filingHistory: String? = null,

		@SerializedName("insolvency")
		var insolvency: String? = null,

		@SerializedName("officers")
		var officers: String? = null,

		@SerializedName("persons_with_significant_control")
		var personsWithSignificantControl: String? = null,

		@SerializedName("persons_with_significant_control_statements")
		var personsWithSignificantControlStatements: String? = null,

		@SerializedName("registers")
		var registers: String? = null,

		@SerializedName("self")
		var self: String? = null

) : Parcelable
