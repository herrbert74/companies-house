package com.babestudios.companyinfouk.insolvencies.ui.details.list

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class InsolvencyDetailsVisitableBase : Parcelable {
	abstract fun type(insolvencyDetailsTypeFactory: InsolvencyDetailsAdapter.InsolvencyDetailsTypeFactory): Int
}

@Parcelize
class InsolvencyDetailsTitleVisitable(val insolvencyDetailsTitleItem: InsolvencyDetailsTitleItem)
	: InsolvencyDetailsVisitableBase(), Parcelable {
	override fun type(insolvencyDetailsTypeFactory: InsolvencyDetailsAdapter.InsolvencyDetailsTypeFactory): Int {
		return insolvencyDetailsTypeFactory.type(insolvencyDetailsTitleItem)
	}
}

@Parcelize
class InsolvencyDetailsDateVisitable(val insolvencyDetailsDateItem: InsolvencyDetailsDateItem)
	: InsolvencyDetailsVisitableBase(), Parcelable {
	override fun type(insolvencyDetailsTypeFactory: InsolvencyDetailsAdapter.InsolvencyDetailsTypeFactory): Int {
		return insolvencyDetailsTypeFactory.type(insolvencyDetailsDateItem)
	}
}

@Parcelize
class InsolvencyDetailsPractitionerVisitable(val insolvencyDetailsPractitionerItem: InsolvencyDetailsPractitionerItem)
	: InsolvencyDetailsVisitableBase(), Parcelable {
	override fun type(insolvencyDetailsTypeFactory: InsolvencyDetailsAdapter.InsolvencyDetailsTypeFactory): Int {
		return insolvencyDetailsTypeFactory.type(insolvencyDetailsPractitionerItem)
	}
}
