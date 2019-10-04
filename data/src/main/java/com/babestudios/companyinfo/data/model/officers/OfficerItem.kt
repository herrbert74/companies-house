package com.babestudios.companyinfo.data.model.officers


import android.os.Parcelable
import com.babestudios.companyinfo.data.model.common.Address
import com.babestudios.companyinfo.data.model.common.MonthYear
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class OfficerItem(
		@SerializedName("address")
		var address: Address? = null,
		@SerializedName("identification")
		var identification: Identification? = null,
		@SerializedName("appointed_on")
		var appointedOn: String? = null,
		@SerializedName("links")
		var links: OfficerLinks? = null,
		@SerializedName("name")
		var name: String? = null,
		@SerializedName("officer_role")
		var officerRole: String? = null,
		@SerializedName("date_of_birth")
		var dateOfBirth: MonthYear? = null,
		@SerializedName("occupation")
		var occupation: String = "Unknown",
		@SerializedName("country_of_residence")
		var countryOfResidence: String = "Unknown",
		@SerializedName("nationality")
		var nationality: String = "Unknown",
		@SerializedName("resigned_on")
		var resignedOn: String? = null
):Parcelable
