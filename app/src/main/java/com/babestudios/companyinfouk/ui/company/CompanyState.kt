package com.babestudios.companyinfouk.ui.company

import com.babestudios.base.mvp.BaseState
import com.babestudios.companyinfouk.data.model.company.Company

enum class ContentChange {
	NONE,
	HIDE_FAB, //Hide fab by scaling, just to scale it back with the new state
	IS_FAVORITE,
	COMPANY_RECEIVED
}

data class CompanyState(
		var company: Company = Company()
) : BaseState() {
	var companyNumber: String = ""
	var companyName: String = ""
	var addressString: String = ""
	var natureOfBusinessString = ""
	var isFavorite = false
	var contentChange: ContentChange = ContentChange.NONE
}