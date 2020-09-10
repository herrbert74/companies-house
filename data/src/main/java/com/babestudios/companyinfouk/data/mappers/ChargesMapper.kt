package com.babestudios.companyinfouk.data.mappers

import com.babestudios.companyinfouk.common.model.charges.Charges
import com.babestudios.companyinfouk.common.model.charges.ChargesItem
import com.babestudios.companyinfouk.common.model.charges.Particulars
import com.babestudios.companyinfouk.common.model.charges.Transaction
import com.babestudios.companyinfouk.data.local.apilookup.ChargesHelperContract
import com.babestudios.companyinfouk.data.model.charges.ChargesDto
import com.babestudios.companyinfouk.data.model.charges.ChargesItemDto
import com.babestudios.companyinfouk.data.model.charges.ParticularsDto
import com.babestudios.companyinfouk.data.model.charges.TransactionDto

inline fun mapChargesDto(
		input: ChargesDto,
		mapItems: (List<ChargesItemDto>?) -> List<ChargesItem>
): Charges {
	return Charges(
			mapItems(input.items),
			input.totalCount ?: 0,
	)
}

fun mapChargesItemDto(
		input: ChargesItemDto,
		chargesHelper: ChargesHelperContract,
		mapTransactionsDto: (List<TransactionDto>?) -> List<Transaction>,
		mapParticulars: (ParticularsDto?) -> Particulars,
): ChargesItem {
	return ChargesItem(
			input.chargeCode.orEmpty(),
			input.createdOn.orEmpty(),
			input.deliveredOn.orEmpty(),
			input.personsEntitled[0].name.orEmpty(),
			input.resolvedOn.orEmpty(),
			input.satisfiedOn.orEmpty(),
			chargesHelper.statusLookUp(input.status.orEmpty()),
			mapTransactionsDto(input.transactions),
			mapParticulars(input.particulars),
	)
}

fun mapTransactionDto(
		input: TransactionDto,
		chargesHelper: ChargesHelperContract,
): Transaction {
	return Transaction(
			input.deliveredOn.orEmpty(),
			chargesHelper.filingTypeLookUp(input.filingType.orEmpty())
	)
}

fun mapParticularsDto(
		input: ParticularsDto?,
): Particulars {
	return Particulars(
			input?.containsFixedCharge,
			input?.containsFloatingCharge,
			input?.containsNegativePledge,
			input?.description ?: "",
			input?.floatingChargeCoversAll,
			input?.type ?: "",
	)
}
