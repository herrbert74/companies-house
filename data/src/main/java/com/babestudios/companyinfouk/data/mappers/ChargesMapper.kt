package com.babestudios.companyinfouk.data.mappers

import com.babestudios.companyinfouk.common.model.charges.Charges
import com.babestudios.companyinfouk.common.model.charges.ChargesItem
import com.babestudios.companyinfouk.common.model.charges.Transaction
import com.babestudios.companyinfouk.common.model.filinghistory.Category
import com.babestudios.companyinfouk.common.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.common.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.common.model.filinghistory.FilingHistoryLinks
import com.babestudios.companyinfouk.data.local.apilookup.ChargesHelper
import com.babestudios.companyinfouk.data.local.apilookup.FilingHistoryDescriptionsHelper
import com.babestudios.companyinfouk.data.model.charges.ChargesDto
import com.babestudios.companyinfouk.data.model.charges.ChargesItemDto
import com.babestudios.companyinfouk.data.model.charges.TransactionDto
import com.babestudios.companyinfouk.data.model.filinghistory.CategoryDto
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryDto
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItemDto
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryLinksDto

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
		mapTransactions: (List<TransactionDto>?) -> List<Transaction>,
): ChargesItem {
	return ChargesItem(
			input.chargeCode.orEmpty(),
			input.createdOn.orEmpty(),
			input.deliveredOn.orEmpty(),
			input.personsEntitled[0].name.orEmpty(),
			input.resolvedOn.orEmpty(),
			input.satisfiedOn.orEmpty(),
			input.status.orEmpty(),
			mapTransactions(input.transactions)
	)
}

fun mapTransactionDto(
		input: TransactionDto,
		chargesHelper: ChargesHelper,
): Transaction {
	return Transaction(
			input.deliveredOn.orEmpty(),
			chargesHelper.filingTypeLookUp(input.filingType.orEmpty())
	)
}
