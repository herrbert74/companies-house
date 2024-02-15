package com.babestudios.companyinfouk.insolvencies.ui.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.babestudios.companyinfouk.common.compose.HeaderCollapsingToolbarScaffold
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.Dimens
import com.babestudios.companyinfouk.insolvencies.R
import com.babestudios.companyinfouk.shared.domain.INSOLVENCY_DATES
import com.babestudios.companyinfouk.shared.domain.INSOLVENCY_PRACTITIONERS
import com.babestudios.companyinfouk.shared.domain.model.common.Address
import com.babestudios.companyinfouk.shared.domain.model.insolvency.Date
import com.babestudios.companyinfouk.shared.domain.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.shared.domain.model.insolvency.Practitioner
import com.babestudios.companyinfouk.shared.screen.insolvencydetails.InsolvencyDetailsComp

@Composable
fun InsolvencyDetailsListScreen(component: InsolvencyDetailsComp) {

	val insolvencyCase = component.insolvencyCase

	BackHandler(onBack = { component.onBackClicked() })

	HeaderCollapsingToolbarScaffold(
		headerBackgroundResource = R.drawable.bg_insolvency,
		navigationAction = { component.onBackClicked() },
		topAppBarActions = {},
		title = stringResource(R.string.insolvency_details)
	) {
		InsolvencyDetailsList(
			items = insolvencyCase,
			onItemClicked = component::onItemClicked
		)
	}

}

@Composable
private fun InsolvencyDetailsList(
	items: InsolvencyCase,
	onItemClicked: (id: Practitioner) -> Unit,
) {

	Column {

		val listState = rememberLazyListState()
		val viewMarginNormal = Dimens.marginNormal
		val viewMarginLarge = Dimens.marginLarge

		LazyColumn(state = listState) {
			itemsIndexed(items.dates) { index, date ->

				if (index == 0) {
					Text(
						modifier = Modifier.padding(
							start = viewMarginLarge,
							top = viewMarginNormal,
							bottom = viewMarginLarge
						),
						text = INSOLVENCY_DATES,
						style = CompaniesTypography.titleSmall
					)
				}
				InsolvencyDateListItem(item = date)
				HorizontalDivider()
			}
		}

		LazyColumn(state = listState) {
			itemsIndexed(items.practitioners) { index, practitioner ->
				if (index == 0) {
					Text(
						modifier = Modifier.padding(
							start = viewMarginLarge,
							top = viewMarginLarge,
							bottom = viewMarginLarge
						),
						text = INSOLVENCY_PRACTITIONERS,
						style = CompaniesTypography.titleSmall
					)
				}
				PractitionerListItem(item = practitioner, onItemClicked)
				HorizontalDivider()
			}
		}

	}

}

@Preview("InsolvencyDetailsList Preview")
@Composable
fun InsolvencyDetailsListPreview() {
	InsolvencyDetailsList(
		items = InsolvencyCase(
			dates = listOf(
				Date(date = "1995-01-18", type = "wound-up-on"),
				Date(date = "1994-12-12", type = "petitioned-on")
			),
			practitioners = listOf(
				Practitioner(
					name = "Alan Redvers Price",
					address = Address(
						addressLine1 = "Price & Co",
						addressLine2 = "P O Box 5895",
						locality = "Wellingborought",
						region = "Northants",
						postalCode = "NN8 5ZD"
					),
					role = "practitioner",
					appointedOn = "1994-12-12",
					ceasedToActOn = "1995-01-18"
				)
			),
		),
		onItemClicked = {}
	)
}

