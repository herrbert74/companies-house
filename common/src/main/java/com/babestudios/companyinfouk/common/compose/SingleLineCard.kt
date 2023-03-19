package com.babestudios.companyinfouk.common.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.babestudios.companyinfouk.common.R
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.titleLargeBold

@Composable
fun SingleLineCard(
	@DrawableRes vectorImageResource: Int,
	text: String,
	modifier: Modifier = Modifier,
	textStyle: TextStyle = CompaniesTypography.titleLargeBold,
) {

	val viewMarginLarge = dimensionResource(com.babestudios.base.R.dimen.viewMarginLarge)
	val viewMarginNormal = dimensionResource(com.babestudios.base.R.dimen.viewMargin)

	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center,
		modifier = modifier
	) {
		Icon(
			painter = painterResource(vectorImageResource),
			modifier = Modifier.padding(start = viewMarginLarge, top = viewMarginNormal, bottom =  viewMarginNormal),
			contentDescription = "Finish",
			tint = MaterialTheme.colorScheme.onSurface
		)
		Text(
			modifier = Modifier
				.padding(horizontal = viewMarginNormal, vertical = viewMarginNormal)
				.fillMaxWidth(1f),
			text = text,
			style = textStyle,
		)
	}
}

@Preview("Single Line Card Preview")
@Composable
fun SingleLineCardPreview() {
	SingleLineCard(
		vectorImageResource = R.drawable.ic_commit,
		text = "Longyearbien Longlongyear",
	)
}
