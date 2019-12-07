package com.babestudios.companyinfouk.officers.ui.officers.list

import android.os.Parcelable
import com.babestudios.companyinfouk.data.model.officers.OfficerItem
import kotlinx.android.parcel.Parcelize

abstract class AbstractOfficersVisitable : Parcelable {
	abstract fun type(officersTypeFactory: OfficersAdapter.OfficersTypeFactory): Int
}

@Parcelize
class OfficersVisitable(val officerItem: OfficerItem) : AbstractOfficersVisitable(), Parcelable {
	override fun type(officersTypeFactory: OfficersAdapter.OfficersTypeFactory): Int {
		return officersTypeFactory.type(officerItem)
	}
}
