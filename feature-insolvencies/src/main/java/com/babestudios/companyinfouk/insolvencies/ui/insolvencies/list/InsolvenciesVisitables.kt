package com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list

import android.os.Parcelable
import com.babestudios.companyinfouk.domain.model.insolvency.InsolvencyCase
import kotlinx.android.parcel.Parcelize

sealed class InsolvencyVisitableBase {
	abstract fun type(insolvenciesTypeFactory: InsolvenciesAdapter.InsolvencyTypeFactory): Int
}

@Parcelize
class InsolvencyVisitable(val insolvencyCase: InsolvencyCase) : InsolvencyVisitableBase(), Parcelable {
	override fun type(insolvenciesTypeFactory: InsolvenciesAdapter.InsolvencyTypeFactory): Int {
		return insolvenciesTypeFactory.type(insolvencyCase)
	}
}
