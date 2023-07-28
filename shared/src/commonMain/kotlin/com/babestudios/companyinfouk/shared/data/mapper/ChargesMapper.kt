package com.babestudios.companyinfouk.shared.data.mapper

import com.babestudios.base.data.mapNullInputList
import com.babestudios.companyinfouk.shared.data.local.apilookup.ChargesHelper
import com.babestudios.companyinfouk.shared.data.model.charges.ChargesDto
import com.babestudios.companyinfouk.shared.data.model.charges.ChargesItemDto
import com.babestudios.companyinfouk.shared.data.model.charges.ParticularsDto
import com.babestudios.companyinfouk.shared.data.model.charges.TransactionDto
import com.babestudios.companyinfouk.shared.domain.model.charges.Charges
import com.babestudios.companyinfouk.shared.domain.model.charges.ChargesItem
import com.babestudios.companyinfouk.shared.domain.model.charges.Particulars
import com.babestudios.companyinfouk.shared.domain.model.charges.Transaction

fun ChargesDto.toCharges() = Charges(
	items.mapChargesList(),
	totalCount ?: 0,
)

private fun List<ChargesItemDto>?.mapChargesList() = mapNullInputList(this) { chargesItemDto ->
	chargesItemDto.toChargesItem(
		{ transactionDtoList ->
			mapNullInputList(transactionDtoList) { transactionsDto ->
				transactionsDto.deliveredOn.orEmpty()
				transactionsDto.toTransaction()
			}
		}
	) { particularsDto ->
		particularsDto.toParticulars()
	}
}

fun ChargesItemDto.toChargesItem(
	mapTransactionsDto: (List<TransactionDto>?) -> List<Transaction>,
	mapParticulars: (ParticularsDto?) -> Particulars,
): ChargesItem {
	return ChargesItem(
		chargeCode.orEmpty(),
		createdOn.orEmpty(),
		deliveredOn.orEmpty(),
		personsEntitled?.get(0)?.name.orEmpty(),
		resolvedOn.orEmpty(),
		satisfiedOn.orEmpty(),
		ChargesHelper.statusLookUp(status.orEmpty()),
		mapTransactionsDto(transactions),
		mapParticulars(particulars),
	)
}

fun TransactionDto.toTransaction() = Transaction(
	deliveredOn.orEmpty(),
	ChargesHelper.filingTypeLookUp(filingType.orEmpty())
)

fun ParticularsDto?.toParticulars() = Particulars(
	this?.containsFixedCharge,
	this?.containsFloatingCharge,
	this?.containsNegativePledge,
	this?.description ?: "",
	this?.floatingChargeCoversAll,
	this?.type ?: "",
)
