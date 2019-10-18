package com.babestudios.companyinfouk.officers.ui.details

import com.airbnb.mvrx.MvRxState
import com.babestudios.companyinfouk.data.model.officers.OfficerItem

enum class ContentChange {
	NONE,
	OFFICER_ITEM_RECEIVED
}

data class OfficerDetailsState(
		var officerItem: OfficerItem? = null,
		var officerId: String = "",
		var contentChange: ContentChange = ContentChange.NONE
) : MvRxState