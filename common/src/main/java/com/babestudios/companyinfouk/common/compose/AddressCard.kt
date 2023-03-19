package com.babestudios.companyinfouk.common.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.babestudios.base.R
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.titleLargeBold
import com.babestudios.companyinfouk.domain.HALF
import com.babestudios.companyinfouk.domain.model.common.Address

@Composable
fun AddressCard(
	modifier: Modifier = Modifier,
	address: Address,
	title: String = "Address",
	onShowMap: () -> Unit,
) {

	val viewMarginLarge = dimensionResource(R.dimen.viewMarginLarge)
	val viewMarginNormal = dimensionResource(R.dimen.viewMargin)

	ConstraintLayout(
		modifier = modifier
			.fillMaxWidth(1f)
			.wrapContentHeight(Alignment.CenterVertically),
	) {
		val (titleText, showMapButton) = createRefs()

		Column(
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.Start,
			modifier = modifier
				.fillMaxWidth(HALF)
				.constrainAs(titleText) {
					top.linkTo(parent.top)
					bottom.linkTo(parent.bottom)
					start.linkTo(parent.start)
				}
		) {
			Text(
				modifier = Modifier
					.padding(horizontal = viewMarginLarge + viewMarginNormal, vertical = viewMarginNormal)
					.fillMaxWidth(1f),
				text = title,
				style = CompaniesTypography.bodyMedium
			)
			Text(
				modifier = Modifier
					.padding(horizontal = viewMarginLarge, vertical = viewMarginNormal)
					.fillMaxWidth(1f),
				text = address.addressLine1,
				style = CompaniesTypography.titleLargeBold
			)
			address.addressLine2?.let {
				Text(
					modifier = Modifier
						.padding(horizontal = viewMarginLarge, vertical = viewMarginNormal)
						.fillMaxWidth(1f),
					text = it,
					style = CompaniesTypography.titleLargeBold,
				)
			}
			Text(
				modifier = Modifier
					.padding(horizontal = viewMarginLarge, vertical = viewMarginNormal)
					.fillMaxWidth(1f),
				text = address.locality,
				style = CompaniesTypography.titleLargeBold
			)
			Text(
				modifier = Modifier
					.padding(horizontal = viewMarginLarge, vertical = viewMarginNormal)
					.fillMaxWidth(1f),
				text = address.postalCode,
				style = CompaniesTypography.titleLargeBold
			)
			address.region?.let {
				Text(
					modifier = Modifier
						.padding(horizontal = viewMarginLarge, vertical = viewMarginNormal)
						.fillMaxWidth(1f),
					text = it,
					style = CompaniesTypography.titleLargeBold
				)
			}
			address.country?.let {
				Text(
					modifier = Modifier
						.padding(horizontal = viewMarginLarge, vertical = viewMarginNormal)
						.fillMaxWidth(1f),
					text = it,
					style = CompaniesTypography.titleLargeBold,
				)
			}
		}

		Button(
			onClick = { onShowMap.invoke() },
			modifier = modifier
				.padding(bottom = 8.dp, end = 8.dp)
				.constrainAs(showMapButton) {
					bottom.linkTo(parent.bottom)
					end.linkTo(parent.end)
				},
		) {
			Text(text = "Show on map".uppercase())
		}
	}
}

@Preview("PractitionerDetails Preview")
@Composable
fun PractitionerDetailsScreenPreview() {
	CompaniesTheme {
		AddressCard(
			address = Address(
				addressLine1 = "Suite A",
				addressLine2 = "4-6 Canfield Place",
				locality = "London",
				postalCode = "NW6 3BT",
				country = "United Kingdom"
			),
			onShowMap = { }
		)
	}
}
