package com.babestudios.companyinfouk.charges.ui.charges

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.babestudios.base.compose.InfiniteListHandler
import com.babestudios.base.compose.simpleVerticalScrollbar
import com.babestudios.companyinfouk.charges.R
import com.babestudios.companyinfouk.common.compose.CollapsingToolbarScaffold
import com.babestudios.companyinfouk.design.Colors
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.shared.domain.model.charges.ChargesItem
import com.babestudios.companyinfouk.shared.domain.model.charges.Particulars
import com.babestudios.companyinfouk.shared.screen.charges.ChargesComp

@Composable
fun ChargesScreen(component: ChargesComp) {

	val model by component.state.subscribeAsState()

	BackHandler(onBack = { component.onBackClicked() })

	CollapsingToolbarScaffold(
		backgroundDrawable = R.drawable.bg_charges,
		title = stringResource(com.babestudios.companyinfouk.common.R.string.charges),
		onBackClicked = { component.onBackClicked() },
	) { paddingValues ->
		if (model.isLoading) {
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier.fillMaxSize().padding(paddingValues)
			) {
				CircularProgressIndicator()
			}
		} else if (model.error != null) {
			Box(Modifier.background(color = Color.Red).padding(paddingValues))
		} else {
			ChargesList(
				items = model.chargesResponse.items,
				paddingValues = paddingValues,
				onItemClicked = component::onItemClicked,
				onLoadMore = component::onLoadMore,
			)
		}
	}
}

@Composable
private fun ChargesList(
	items: List<ChargesItem>,
	paddingValues: PaddingValues,
	onItemClicked: (id: ChargesItem) -> Unit,
	onLoadMore: () -> Unit,
) {
	Box(modifier = Modifier.padding(paddingValues)) {
		val listState = rememberLazyListState()

		LazyColumn(
			Modifier.simpleVerticalScrollbar(listState),
			state = listState
		) {
			itemsIndexed(
				items = items,
				key = { _, item -> item.hashCode() }
			) { _, chargesItem ->
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

@PreviewLightDark
@Composable
fun ChargesListPreview() {
	CompaniesTheme {
		Box(Modifier.background(color = Colors.surface)) {
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
				paddingValues = PaddingValues(),
				onItemClicked = {},
				onLoadMore = {}
			)
		}
	}

}