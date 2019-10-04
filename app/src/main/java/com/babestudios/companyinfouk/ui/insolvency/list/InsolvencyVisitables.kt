package com.babestudios.companyinfouk.ui.insolvency.list

import android.os.Parcelable
import com.babestudios.companyinfo.data.model.insolvency.InsolvencyCase
import kotlinx.android.parcel.Parcelize

abstract class AbstractInsolvencyVisitable {
	abstract fun type(insolvencyTypeFactory: InsolvencyAdapter.InsolvencyTypeFactory): Int
}

@Parcelize
class InsolvencyVisitable(val insolvencyCase: InsolvencyCase) : AbstractInsolvencyVisitable(), Parcelable {
	override fun type(insolvencyTypeFactory: InsolvencyAdapter.InsolvencyTypeFactory): Int {
		return insolvencyTypeFactory.type(insolvencyCase)
	}
}