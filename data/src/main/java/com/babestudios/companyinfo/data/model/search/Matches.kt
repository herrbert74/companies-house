package com.babestudios.companyinfo.data.model.search

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList

@Parcelize
class Matches (

	@SerializedName("title")
	var title: List<Int> = ArrayList(),

	@SerializedName("snippet")
	var snippet: List<String> = ArrayList()

): Parcelable
