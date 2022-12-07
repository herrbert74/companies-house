package com.babestudios.companyinfouk.common.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.titleLargeBold

@Composable
fun TwoLineCard(
	firstLineString: String,
	secondLineString: String,
	modifier: Modifier = Modifier,
) {
	Column(
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier
	) {
		Text(
			modifier = Modifier
				.padding(horizontal = 8.dp, vertical = 8.dp)
				.fillMaxWidth(1f),
			text = firstLineString,
			style = CompaniesTypography.bodyMedium
		)
		Text(
			modifier = Modifier
				.padding(horizontal = 8.dp, vertical = 8.dp)
				.fillMaxWidth(1f),
			text = secondLineString,
			style = CompaniesTypography.titleLargeBold
		)
	}
}
