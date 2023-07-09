package com.babestudios.companyinfouk.shared.data.model.charges

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParticularsDto(
	@SerialName("chargor_acting_as_bare_trustee")
	var chargorActingAsBareTrustee: String? = null,
	@SerialName("contains_fixed_charge")
	var containsFixedCharge: Boolean? = null,
	@SerialName("contains_floating_charge")
	var containsFloatingCharge: Boolean? = null,
	@SerialName("contains_negative_pledge")
	var containsNegativePledge: Boolean? = null,
	@SerialName("description")
	var description: String? = null,
	@SerialName("floating_charge_covers_all")
	var floatingChargeCoversAll: Boolean? = null,
	@SerialName("type")
	var type: String? = null,
)
