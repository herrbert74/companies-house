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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.Dimens
import com.babestudios.companyinfouk.design.titleLargeBold

//TODO Replace vectorImageResource with ImageVector
@Composable
fun SingleLineCard(
	@DrawableRes vectorImageResource: Int,
	text: String,
	modifier: Modifier = Modifier,
	textStyle: TextStyle = CompaniesTypography.titleLargeBold,
) {

	val viewMarginLarge = Dimens.marginLarge
	val viewMarginNormal = Dimens.marginNormal

	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center,
		modifier = modifier
	) {
		Icon(
			painter = painterResource(vectorImageResource),
			modifier = Modifier.padding(start = viewMarginLarge, top = viewMarginNormal, bottom =  viewMarginNormal),
			contentDescription = text,
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
		vectorImageResource = android.R.drawable.ic_menu_add,
		text = "Longyearbien Longlongyear",
	)
}
