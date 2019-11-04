package com.babestudios.companyinfouk.insolvencies.ui.details.list

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

abstract class AbstractInsolvencyDetailsVisitable : Parcelable {
	abstract fun type(insolvencyDetailsTypeFactory: InsolvencyDetailsAdapter.InsolvencyDetailsTypeFactory): Int
}

@Parcelize
class InsolvencyDetailsTitleVisitable(val insolvencyDetailsTitleItem: InsolvencyDetailsTitleItem) : AbstractInsolvencyDetailsVisitable(), Parcelable {
	override fun type(insolvencyDetailsTypeFactory: InsolvencyDetailsAdapter.InsolvencyDetailsTypeFactory): Int {
		return insolvencyDetailsTypeFactory.type(insolvencyDetailsTitleItem)
	}
}

@Parcelize
class InsolvencyDetailsDateVisitable(val insolvencyDetailsDateItem: InsolvencyDetailsDateItem) : AbstractInsolvencyDetailsVisitable(), Parcelable {
	override fun type(insolvencyDetailsTypeFactory: InsolvencyDetailsAdapter.InsolvencyDetailsTypeFactory): Int {
		return insolvencyDetailsTypeFactory.type(insolvencyDetailsDateItem)
	}
}

@Parcelize
class InsolvencyDetailsPractitionerVisitable(val insolvencyDetailsPractitionerItem: InsolvencyDetailsPractitionerItem) : AbstractInsolvencyDetailsVisitable(), Parcelable {
	override fun type(insolvencyDetailsTypeFactory: InsolvencyDetailsAdapter.InsolvencyDetailsTypeFactory): Int {
		return insolvencyDetailsTypeFactory.type(insolvencyDetailsPractitionerItem)
	}
}