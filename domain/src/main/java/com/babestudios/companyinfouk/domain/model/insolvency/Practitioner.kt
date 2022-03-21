package com.babestudios.companyinfouk.domain.model.insolvency


import android.os.Parcelable
import com.babestudios.companyinfouk.domain.model.common.Address
import kotlinx.parcelize.Parcelize

@Parcelize
class Practitioner(
	var address: Address = Address(),
	var appointedOn: String?,
	var ceasedToActOn: String?,
	var name: String = "",
	var role: String?,
) : Parcelable
