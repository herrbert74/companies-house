package com.babestudios.companyinfouk.shared.data.model.charges

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class InsolvencyLinksDto {

	inner class Links {
		@SerialName("case")
		var case: String? = null
	}

}
