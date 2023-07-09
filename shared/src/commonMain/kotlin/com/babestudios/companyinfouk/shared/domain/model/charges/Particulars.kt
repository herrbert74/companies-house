package com.babestudios.companyinfouk.shared.domain.model.charges

import com.babestudios.companyinfouk.shared.Parcelable
import com.babestudios.companyinfouk.shared.Parcelize

@Parcelize
class Particulars(
	var containsFixedCharge: Boolean? = false,
	var containsFloatingCharge: Boolean? = false,
	var containsNegativePledge: Boolean? = false,
	var description: String = "",
	var floatingChargeCoversAll: Boolean? = false,
	var type: String = ""
) : Parcelable
