package com.babestudios.companyinfouk.shared.domain.model.insolvency

import com.babestudios.companyinfouk.shared.Parcelable
import com.babestudios.companyinfouk.shared.Parcelize

@Parcelize
class InsolvencyCase (
	var dates: List<Date> = ArrayList(),
	var practitioners: List<Practitioner> = ArrayList(),
	var type: String? = null
): Parcelable
