package com.babestudios.companyinfouk.charges.ui.charges.list

import android.os.Parcelable
import com.babestudios.companyinfouk.data.model.charges.ChargesItem
import kotlinx.android.parcel.Parcelize

abstract class AbstractChargesVisitable {
	abstract fun type(chargesTypeFactory: ChargesAdapter.ChargesTypeFactory): Int
}

@Parcelize
class ChargesVisitable(val chargesItem: ChargesItem) : AbstractChargesVisitable(), Parcelable {
	override fun type(chargesTypeFactory: ChargesAdapter.ChargesTypeFactory): Int {
		return chargesTypeFactory.type(chargesItem)
	}
}