package com.babestudios.companyinfouk.data.model.charges


import android.os.Parcelable
import androidx.annotation.Keep
import com.babestudios.companyinfouk.data.model.common.SelfLinkDataDto
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ChargesItemDto(
		@SerializedName("acquired_on")
		var acquiredOn: String? = null,
		@SerializedName("assets_ceased_released")
		var assetsCeasedReleased: String? = null,
		@SerializedName("charge_code")
		@Keep var chargeCode: String? = null,
		@SerializedName("charge_number")
		var chargeNumber: String? = null,
		@SerializedName("classification")
		var classification: ClassificationDto? = null,
		@SerializedName("covering_instrument_date")
		var coveringInstrumentDate: String? = null,
		@SerializedName("created_on")
		var createdOn: String? = null,
		@SerializedName("delivered_on")
		var deliveredOn: String? = null,
		@SerializedName("etag")
		var etag: String? = null,
		@SerializedName("id")
		var id: String? = null,
		@SerializedName("insolvency_cases")
		var insolvencyCases: List<InsolvencyCaseDto> = ArrayList(),
		@SerializedName("links")
		var links: SelfLinkDataDto? = null,
		@SerializedName("more_than_four_persons_entitled")
		var moreThanFourPersonsEntitled: String? = null,
		@SerializedName("particulars")
		var particulars: ParticularsDto? = null,
		@SerializedName("persons_entitled")
		var personsEntitled: List<PersonsEntitledDto> = ArrayList(),
		@SerializedName("resolved_on")
		var resolvedOn: String? = null,
		@SerializedName("satisfied_on")
		var satisfiedOn: String? = null,
		@SerializedName("scottish_alterations")
		var scottishAlterations: ScottishAlterationsDto? = null,
		@SerializedName("secured_details")
		var securedDetails: SecuredDetailsDto? = null,
		@SerializedName("status")
		var status: String? = null,
		@SerializedName("transactions")
		var transactions: List<TransactionDto> = ArrayList()
) : Parcelable
