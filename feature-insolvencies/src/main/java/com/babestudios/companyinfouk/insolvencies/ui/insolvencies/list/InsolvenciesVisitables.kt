package com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list

import android.os.Parcelable
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyCase
import kotlinx.android.parcel.Parcelize

@Suppress("UnnecessaryAbstractClass")
abstract class AbstractInsolvencyVisitable {
	abstract fun type(insolvenciesTypeFactory: InsolvenciesAdapter.InsolvencyTypeFactory): Int
}

@Parcelize
class InsolvencyVisitable(val insolvencyCase: InsolvencyCase) : AbstractInsolvencyVisitable(), Parcelable {
	override fun type(insolvenciesTypeFactory: InsolvenciesAdapter.InsolvencyTypeFactory): Int {
		return insolvenciesTypeFactory.type(insolvencyCase)
	}
}
