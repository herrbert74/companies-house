package com.babestudios.companyinfouk.domain.model.charges

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Particulars(
	var containsFixedCharge: Boolean? = false,
	var containsFloatingCharge: Boolean? = false,
	var containsNegativePledge: Boolean? = false,
	var description: String = "",
	var floatingChargeCoversAll: Boolean? = false,
	var type: String = ""
) : Parcelable
