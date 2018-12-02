package com.babestudios.companyinfouk.data.model.officers.appointments


import com.babestudios.companyinfouk.data.model.officers.Address
import com.babestudios.companyinfouk.data.model.officers.Identification
import com.google.gson.annotations.SerializedName

class Appointment {

	@SerializedName("address")
	var address: Address? = null
	@SerializedName("appointed_before")
	var appointedBefore: String? = null
	@SerializedName("appointed_on")
	var appointedOn: String? = null
	@SerializedName("appointed_to")
	var appointedTo: AppointedTo? = null
	@SerializedName("country_of_residence")
	var countryOfResidence: String? = null
	@SerializedName("former_names")
	var formerNames: List<FormerName>? = null
	@SerializedName("identification")
	var identification: Identification? = null
	@SerializedName("is_pre_1992_appointment")
	var isPre1992Appointment: String? = null
	@SerializedName("links")
	var links: Links? = null
	@SerializedName("name")
	var name: String? = null
	@SerializedName("name_elements")
	var nameElements: NameElements? = null
	@SerializedName("nationality")
	var nationality: String? = null
	@SerializedName("occupation")
	var occupation: String? = null
	@SerializedName("officer_role")
	var officerRole: String? = null
	@SerializedName("resigned_on")
	var resignedOn: String? = null

}
