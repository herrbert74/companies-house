@file:Suppress("UNUSED_PARAMETER, FunctionNaming")

package com.babestudios.companyinfouk.insolvencies.ui.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.domain.model.insolvency.Date
import com.babestudios.companyinfouk.domain.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.domain.model.insolvency.Practitioner
import com.babestudios.companyinfouk.insolvencies.R

@Composable
fun InsolvencyDetailsListScreen(component: InsolvencyDetailsListComp) {

	val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
	val insolvencyCase = component.insolvencyCase
	val title = stringResource(R.string.insolvency_details)

	BackHandler(onBack = { component.onBackClicked() })

	Scaffold(
		modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
		topBar = {
			LargeTopAppBar(
				title = {
					Text(
						text = title, style = CompaniesTypography.titleLarge
					)
				},
				navigationIcon = {
					IconButton(onClick = { component.onBackClicked() }) {
						Icon(
							imageVector = Icons.Filled.ArrowBack,
							contentDescription = "Localized description"
						)
					}
				},
				//Add back image background oce supported
				//app:imageViewSrc="@drawable/bg_InsolvencyDetails"
				scrollBehavior = scrollBehavior
			)
		},
		content = { innerPadding ->
			InsolvencyDetailsList(
				paddingValues = innerPadding,
				items = insolvencyCase,
				onItemClicked = component::onItemClicked
			)
		})

}

@Composable
private fun InsolvencyDetailsList(
	paddingValues: PaddingValues,
	items: InsolvencyCase,
	onItemClicked: (id: Practitioner) -> Unit,
) {

	Column(
		Modifier.padding(paddingValues),
	) {

		val listState = rememberLazyListState()
		val viewMarginLarge = dimensionResource(R.dimen.viewMarginLarge)
		val viewMarginNormal = dimensionResource(R.dimen.viewMargin)

		LazyColumn(state = listState) {
			itemsIndexed(items.dates) { index, date ->

				if (index == 0) {
					Text(
						modifier = Modifier.padding(
							start = viewMarginLarge,
							top = viewMarginNormal,
							bottom = viewMarginLarge
						),
						text = stringResource(R.string.insolvency_dates),
						style = CompaniesTypography.titleSmall
					)
				}
				InsolvencyDateListItem(item = date)
				Divider()
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
						text = stringResource(R.string.insolvency_practitioners),
						style = CompaniesTypography.titleSmall
					)
				}
				PractitionerListItem(item = practitioner, onItemClicked)
				Divider()
			}
		}

	}

}

@Preview("InsolvencyDetailsList Preview")
@Composable
fun InsolvencyDetailsListPreview() {
	InsolvencyDetailsList(
		paddingValues = PaddingValues(),
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

