package com.babestudios.companyinfouk.data.model.charges

import android.os.Parcelable
import androidx.annotation.Keep
import com.babestudios.companyinfouk.data.model.common.SelfLinkDataDto
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ChargesItemDto(
	@SerialName("acquired_on")
	var acquiredOn: String? = null,
	@SerialName("assets_ceased_released")
	var assetsCeasedReleased: String? = null,
	@SerialName("charge_code")
	@Keep var chargeCode: String? = null,
	@SerialName("charge_number")
	var chargeNumber: Int? = null,
	@SerialName("classification")
	var classification: ClassificationDto? = null,
	@SerialName("covering_instrument_date")
	var coveringInstrumentDate: String? = null,
	@SerialName("created_on")
	var createdOn: String? = null,
	@SerialName("delivered_on")
	var deliveredOn: String? = null,
	@SerialName("etag")
	var etag: String? = null,
	@SerialName("id")
	var id: String? = null,
	@SerialName("insolvency_cases")
	var insolvencyCases: List<InsolvencyCaseDto>? = ArrayList(),
	@SerialName("links")
	var links: SelfLinkDataDto? = null,
	@SerialName("more_than_four_persons_entitled")
	var moreThanFourPersonsEntitled: String? = null,
	@SerialName("particulars")
	var particulars: ParticularsDto? = null,
	@SerialName("persons_entitled")
	var personsEntitled: List<PersonsEntitledDto>? = ArrayList(),
	@SerialName("resolved_on")
	var resolvedOn: String? = null,
	@SerialName("satisfied_on")
	var satisfiedOn: String? = null,
	@SerialName("scottish_alterations")
	var scottishAlterations: ScottishAlterationsDto? = null,
	@SerialName("secured_details")
	var securedDetails: SecuredDetailsDto? = null,
	@SerialName("status")
	var status: String? = null,
	@SerialName("transactions")
	var transactions: List<TransactionDto>? = ArrayList(),
) : Parcelable
