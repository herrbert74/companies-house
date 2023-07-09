package com.babestudios.companyinfouk.shared.data.model.insolvency

import com.babestudios.companyinfouk.shared.data.model.common.AddressDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PractitionerDto(
	@SerialName("address")
	var address: AddressDto? = null,
	@SerialName("appointed_on")
	var appointedOn: String? = null,
	@SerialName("ceased_to_act_on")
	var ceasedToActOn: String? = null,
	@SerialName("name")
	var name: String? = null,
	@SerialName("role")
	var role: String? = null,
)
