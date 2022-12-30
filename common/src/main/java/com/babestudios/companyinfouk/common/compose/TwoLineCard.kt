package com.babestudios.companyinfouk.common.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import com.babestudios.companyinfouk.common.R
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.titleLargeBold

@Composable
fun TwoLineCard(
	firstLineString: String,
	secondLineString: String,
	modifier: Modifier = Modifier,
	firstLineStyle: TextStyle = CompaniesTypography.bodyMedium,
	secondLineStyle: TextStyle = CompaniesTypography.titleLargeBold,
) {

	val viewMarginLarge = dimensionResource(R.dimen.viewMarginLarge)
	val viewMarginNormal = dimensionResource(R.dimen.viewMargin)

	Column(
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier
	) {
		Text(
			modifier = Modifier
				.padding(horizontal = viewMarginLarge + viewMarginNormal, vertical = viewMarginNormal)
				.fillMaxWidth(1f),
			text = firstLineString,
			style = firstLineStyle,
		)
		Text(
			modifier = Modifier
				.padding(horizontal = viewMarginLarge, vertical = viewMarginNormal)
				.fillMaxWidth(1f),
			text = secondLineString,
			style = secondLineStyle,
		)
	}
}
