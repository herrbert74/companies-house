package com.babestudios.companyinfouk.common.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.babestudios.companyinfouk.design.Colors
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.Dimens
import com.babestudios.companyinfouk.design.component.BodyMediumText
import com.babestudios.companyinfouk.design.component.TitleLargeBoldText
import com.babestudios.companyinfouk.design.titleLargeBold
import com.babestudios.companyinfouk.shared.domain.HALF
import com.babestudios.companyinfouk.shared.domain.model.common.Address

@Composable
fun AddressCard(
	modifier: Modifier = Modifier,
	address: Address,
	title: String = "Address",
	onShowMap: () -> Unit,
) {

	//val viewMarginNormal = Dimens.marginNormal

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
			BodyMediumText(
				modifier = Modifier
					.padding(horizontal = Dimens.marginLarge + Dimens.marginNormal, vertical = Dimens.marginNormal)
					.fillMaxWidth(1f),
				text = title,
			)
			TitleLargeBoldText(
				modifier = Modifier
					.padding(horizontal = Dimens.marginLarge, vertical = Dimens.marginNormal)
					.fillMaxWidth(1f),
				text = address.addressLine1,
			)
			address.addressLine2?.let {
				TitleLargeBoldText(
					modifier = Modifier
						.padding(horizontal = Dimens.marginLarge, vertical = Dimens.marginNormal)
						.fillMaxWidth(1f),
					text = it,
				)
			}
			TitleLargeBoldText(
				modifier = Modifier
					.padding(horizontal = Dimens.marginLarge, vertical = Dimens.marginNormal)
					.fillMaxWidth(1f),
				text = address.locality,
			)
			TitleLargeBoldText(
				modifier = Modifier
					.padding(horizontal = Dimens.marginLarge, vertical = Dimens.marginNormal)
					.fillMaxWidth(1f),
				text = address.postalCode,
			)
			address.region?.let {
				TitleLargeBoldText(
					modifier = Modifier
						.padding(horizontal = Dimens.marginLarge, vertical = Dimens.marginNormal)
						.fillMaxWidth(1f),
					text = it,
				)
			}
			address.country?.let {
				TitleLargeBoldText(
					modifier = Modifier
						.padding(horizontal = Dimens.marginLarge, vertical = Dimens.marginNormal)
						.fillMaxWidth(1f),
					text = it,
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

@Preview("AddressCard Preview")
@Composable
fun AddressCardPreview() {
	CompaniesTheme {
		Box(Modifier.background(color = Colors.background)) {
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
}

@Preview("AddressCard Dark Preview", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AddressCardDarkPreview() {
	CompaniesTheme {
		Box(Modifier.background(color = Colors.background)) {
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
}
