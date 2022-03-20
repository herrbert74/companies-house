package com.babestudios.companyinfouk.officers.ui.officers.list

import com.babestudios.companyinfouk.domain.model.officers.Officer

sealed class OfficersVisitableBase {
	abstract fun type(officersTypeFactory: OfficersAdapter.OfficersTypeFactory): Int
}

class OfficersVisitable(val officerItem: Officer) : OfficersVisitableBase() {
	override fun type(officersTypeFactory: OfficersAdapter.OfficersTypeFactory): Int {
		return officersTypeFactory.type(officerItem)
	}
}
