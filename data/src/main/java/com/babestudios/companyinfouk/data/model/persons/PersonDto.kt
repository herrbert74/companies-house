package com.babestudios.companyinfouk.data.model.persons


import android.os.Parcelable
import androidx.annotation.Keep
import com.babestudios.companyinfouk.data.model.common.AddressDto
import com.babestudios.companyinfouk.data.model.common.MonthYearDto
import com.babestudios.companyinfouk.data.model.common.SelfLinkDataDto
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class PersonDto(
		@SerializedName("notified_on")
		var notifiedOn: String? = null,
		@SerializedName("ceased_on")
		var ceasedOn: String? = null,
		@SerializedName("kind")
		var kind: String? = null,
		@SerializedName("country_of_residence")
		var countryOfResidence: String? = null,
		@SerializedName("etag")
		var etag: String? = null,
		@SerializedName("date_of_birth")
		var dateOfBirth: MonthYearDto? = null,
		@SerializedName("address")
		var address: AddressDto? = null,
		@SerializedName("links")
		var links: SelfLinkDataDto? = null,
		@SerializedName("name_elements")
		var nameElements: NameElementsDto? = null,
		@SerializedName("natures_of_control")
		var naturesOfControl: List<String> = ArrayList(),
		var nationality: String? = null,
		@Keep var name: String? = null,
		var identification: IdentificationDto? = null
) : Parcelable
