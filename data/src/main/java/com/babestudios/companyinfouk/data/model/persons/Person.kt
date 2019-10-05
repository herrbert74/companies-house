package com.babestudios.companyinfouk.data.model.persons


import android.os.Parcelable
import androidx.annotation.Keep
import com.babestudios.companyinfouk.data.model.common.Address
import com.babestudios.companyinfouk.data.model.common.MonthYear
import com.babestudios.companyinfouk.data.model.common.SelfLinkData
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class Person(
		@SerializedName("notified_on")
		var notifiedOn: String? = null,
		@SerializedName("kind")
		var kind: String? = null,
		@SerializedName("country_of_residence")
		var countryOfResidence: String? = null,
		@SerializedName("etag")
		var etag: String? = null,
		@SerializedName("date_of_birth")
		var dateOfBirth: MonthYear? = null,
		@SerializedName("address")
		var address: Address? = null,
		@SerializedName("links")
		var links: SelfLinkData? = null,
		@SerializedName("name_elements")
		var nameElements: NameElements? = null,
		@SerializedName("natures_of_control")
		var naturesOfControl: List<String> = ArrayList(),
		var nationality: String? = null,
		@Keep var name: String? = null,
		var identification: Identification? = null
) : Parcelable
