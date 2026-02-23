package com.babestudios.companyinfouk.common.compose

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.babestudios.companyinfouk.design.Colors
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.Dimens
import com.babestudios.companyinfouk.design.titleLargeBold

// TODO Replace vectorImageResource with ImageVector
@Composable
fun SingleLineCard(
	@DrawableRes vectorImageResource: Int,
	text: String,
	modifier: Modifier = Modifier,
	textStyle: TextStyle = CompaniesTypography.titleLargeBold.merge(Colors.onBackground),
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
			modifier = Modifier.padding(start = viewMarginLarge, top = viewMarginNormal, bottom = viewMarginNormal),
			contentDescription = text,
			tint = Colors.onBackground
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
private fun SingleLineCardPreview() {
	CompaniesTheme {
		Box(modifier = Modifier.background(Colors.background)) {
			SingleLineCard(
				vectorImageResource = android.R.drawable.ic_popup_reminder,
				text = "Longyearbien Longlongyear",
			)
		}
	}
}

@Preview("Single Line Card Preview Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SingleLineCardDarkPreview() {
	CompaniesTheme {
		Box(modifier = Modifier.background(Colors.background)) {
			SingleLineCard(
				vectorImageResource = android.R.drawable.ic_popup_reminder,
				text = "Longyearbien Longlongyear",
			)
		}
	}
}
