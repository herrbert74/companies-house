package com.babestudios.companyinfouk.data.model.filinghistory


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList

@Parcelize
class FilingHistoryItem(
		@SerializedName("date")
		var date: String? = null,

		@SerializedName("type")
		var type: String? = null,

		@SerializedName("links")
		var links: FilingHistoryLinks? = null,

		@SerializedName("category")
		var category: Category? = null,

		@SerializedName("subcategory")
		var subcategory: String? = null,

		@SerializedName("action_date")
		var actionDate: String? = null,

		@SerializedName("description")
		var description: String? = null,

		@SerializedName("description_values")
		var descriptionValues: DescriptionValues? = null,

		@SerializedName("pages")
		var pages: Int? = null,

		@SerializedName("barcode")
		var barcode: String? = null,

		@SerializedName("transaction_id")
		var transactionId: String? = null,

		@SerializedName("associated_filings")
		var associatedFilings: List<AssociatedFiling> = ArrayList(),

		@SerializedName("paper_filed")
		var paperFiled: Boolean? = null) : Parcelable
