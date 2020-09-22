package com.babestudios.companyinfouk.common.model.insolvency

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize
import java.util.*

@Keep
@Parcelize
class InsolvencyCase (
		var dates: List<Date> = ArrayList(),
		var practitioners: List<Practitioner> = ArrayList(),
		var type: String? = null
): Parcelable
