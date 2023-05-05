package com.babestudios.companyinfouk.domain.model.search

import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize

import java.util.ArrayList
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class Matches (

	@SerialName("title")
	var title: List<Int> = ArrayList(),

	@SerialName("snippet")
	var snippet: List<String> = ArrayList()

): Parcelable
