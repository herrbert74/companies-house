package com.babestudios.companyinfouk.shared.domain.model.insolvency


import com.babestudios.companyinfouk.shared.domain.model.common.Address
import kotlinx.serialization.Serializable

@Serializable
class Practitioner(
	var address: Address = Address(),
	var appointedOn: String?,
	var ceasedToActOn: String?,
	var name: String = "",
	var role: String?,
)
