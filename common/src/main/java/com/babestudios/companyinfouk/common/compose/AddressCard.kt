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
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.titleLargeBold
import com.babestudios.companyinfouk.domain.HALF
import com.babestudios.companyinfouk.domain.model.common.Address

@Composable
fun AddressCard(
	address: Address,
	modifier: Modifier = Modifier,
	onShowMap: () -> Unit,
) {
	ConstraintLayout(
		modifier = modifier
			.fillMaxWidth(1f)
			.wrapContentHeight(Alignment.CenterVertically),
	) {
		val (title, showMapButton) = createRefs()

		Column(
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.Start,
			modifier = modifier
				.fillMaxWidth(HALF)
				.constrainAs(title) {
					top.linkTo(parent.top)
					bottom.linkTo(parent.bottom)
					start.linkTo(parent.start)
				}
		) {
			Text(
				modifier = Modifier
					.padding(horizontal = 8.dp, vertical = 8.dp)
					.fillMaxWidth(1f),
				text = "Address",
				style = CompaniesTypography.bodyMedium
			)
			Text(
				modifier = Modifier
					.padding(horizontal = 8.dp, vertical = 8.dp)
					.fillMaxWidth(1f),
				text = address.addressLine1,
				style = CompaniesTypography.titleLargeBold
			)
			Text(
				modifier = Modifier
					.padding(horizontal = 8.dp, vertical = 8.dp)
					.fillMaxWidth(1f),
				text = address.locality,
				style = CompaniesTypography.titleLargeBold
			)
			Text(
				modifier = Modifier
					.padding(horizontal = 8.dp, vertical = 8.dp)
					.fillMaxWidth(1f),
				text = address.postalCode,
				style = CompaniesTypography.titleLargeBold
			)
			Text(
				modifier = Modifier
					.padding(horizontal = 8.dp, vertical = 8.dp)
					.fillMaxWidth(1f),
				text = address.region ?: "",
				style = CompaniesTypography.titleLargeBold
			)
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
