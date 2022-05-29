package com.babestudios.companyinfouk.domain.model.charges

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChargesItem(
	val chargeCode: String = "",
	var createdOn: String = "",
	var deliveredOn: String = "",
	var personsEntitled: String = "",
	var resolvedOn: String = "",
	var satisfiedOn: String = "",
	var status: String = "",
	var transactions: List<Transaction> = emptyList(),
	val particulars: Particulars = Particulars(),
): Parcelable
