package com.babestudios.companyinfouk.common.model.company

import android.os.Parcelable
import com.babestudios.companyinfouk.common.model.common.Address
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Company(
		val companyName: String = "",
		val lastAccountsMadeUpTo: String = "",
		val companyNumber: String = "",
		val dateOfCreation: String = "",
		val hasCharges: Boolean = false,
		val hasInsolvencyHistory: Boolean = false,
		val registeredOfficeAddress: Address = Address(),
		val natureOfBusiness: String = "",
) : Parcelable
