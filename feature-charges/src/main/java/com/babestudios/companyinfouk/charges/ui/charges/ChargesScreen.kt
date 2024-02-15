package com.babestudios.companyinfouk.charges.ui.charges

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.babestudios.base.compose.InfiniteListHandler
import com.babestudios.base.compose.simpleVerticalScrollbar
import com.babestudios.companyinfouk.charges.R
import com.babestudios.companyinfouk.common.compose.HeaderCollapsingToolbarScaffold
import com.babestudios.companyinfouk.shared.domain.model.charges.ChargesItem
import com.babestudios.companyinfouk.shared.domain.model.charges.Particulars
import com.babestudios.companyinfouk.shared.screen.charges.ChargesComp

@Composable
fun ChargesScreen(component: ChargesComp) {

	val model by component.state.subscribeAsState()

	BackHandler(onBack = { component.onBackClicked() })

	HeaderCollapsingToolbarScaffold(
		headerBackgroundResource = R.drawable.bg_charges,
		navigationAction = { component.onBackClicked() },
		topAppBarActions = {},
		title = stringResource(com.babestudios.companyinfouk.common.R.string.charges)
	) {
		if (model.isLoading) {
			CircularProgressIndicator()
		} else if (model.error != null) {
			Box(Modifier.background(color = Color.Red))
		} else {
			ChargesList(
				items = model.chargesResponse.items,
				onItemClicked = component::onItemClicked,
				onLoadMore = component::onLoadMore,
			)
		}
	}
}

@Composable
private fun ChargesList(
	items: List<ChargesItem>,
	onItemClicked: (id: ChargesItem) -> Unit,
	onLoadMore: () -> Unit,
) {
	Box {
		val listState = rememberLazyListState()

		LazyColumn(
			Modifier.simpleVerticalScrollbar(listState),
			state = listState
		) {
			itemsIndexed(items) { _, chargesItem ->
				ChargesItemListItem(
					item = chargesItem,
					onItemClicked = onItemClicked,
				)

				HorizontalDivider()
			}
		}

		InfiniteListHandler(listState = listState) {
			onLoadMore()
		}

	}

}

@Preview("Charges List Preview")
@Composable
fun ChargesListPreview() {
	ChargesList(
		items = listOf(
			ChargesItem(
				chargeCode = "03453457345734",
				status = "Outstanding",
				deliveredOn = "2011-01-13",
				satisfiedOn = "2012-01-13",
				createdOn = "2011-01-10",
				particulars = Particulars(
					type = "short-particulars",
					containsFixedCharge = true,
					floatingChargeCoversAll = false,
					containsFloatingCharge = true,
					containsNegativePledge = true,
					description = "The subcontracts relating to the vehicles or other goods now or hereafter owned by the lessor" +
						" and comprised in the principal contracts, the full benefit of all monies under the subcontracts, the " +
						"benefit of all guarantees, the benefit of all insurances, the benefit of all supplemental or collateral " +
						"agreements, see image for full details."
				),
				personsEntitled = "Man Financial Services PLC",
			),
			ChargesItem(
				chargeCode = "03453457345733",
				status = "Satisfied",
				deliveredOn = "2011-01-13",
				satisfiedOn = "2012-01-13",
				createdOn = "2011-01-10",
				particulars = Particulars(
					type = "short-particulars",
					containsFixedCharge = true,
					floatingChargeCoversAll = false,
					containsFloatingCharge = true,
					containsNegativePledge = true,
					description = "The subcontracts relating to the vehicles or other goods now or hereafter owned by the lessor" +
						" and comprised in the principal contracts, the full benefit of all monies under the subcontracts, the " +
						"benefit of all guarantees, the benefit of all insurances, the benefit of all supplemental or collateral " +
						"agreements, see image for full details."
				),
				personsEntitled = "Man Financial Services PLC",
			)
		),
		onItemClicked = {},
		onLoadMore = {}
	)
}
