package com.babestudios.companyinfouk.common.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import com.babestudios.base.R
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.titleLargeBold

/**
 * We need both full width and not full width cards.
 * The default needs to be Modifier (so empty),
 * so supply Modifier.fillMaxWidth(1f) as the third parameter, if you need full width,
 * or .weight(1f), if not, but need to fill the empty space.
 */
@Composable
fun TwoLineCard(
	firstLineString: String,
	secondLineString: String,
	modifier: Modifier = Modifier,
	firstLineStyle: TextStyle = CompaniesTypography.bodyMedium,
	secondLineStyle: TextStyle = CompaniesTypography.titleLargeBold,
	/**
	 * By default this displays a normal sized title in the first line,
	 * and a bold, large title in the second, plus the first line has an extra margin.
	 * This can be used for displaying label + data.
	 * If we flip them (for example when we want to show data + less important data),
	 * the additional margin on the first line is removed.
	 */
	flipLineStyles: Boolean = false,
) {

	val viewMarginLarge = dimensionResource(R.dimen.viewMarginLarge)
	val viewMarginNormal = dimensionResource(R.dimen.viewMargin)

	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.Start
	) {

		val startMarginFirst = if (flipLineStyles) viewMarginLarge else viewMarginLarge + viewMarginNormal

		Text(
			modifier = Modifier
				.padding(start = startMarginFirst, top = viewMarginNormal),
				//.fillMaxWidth(1f),
			text = firstLineString,
			style = if (flipLineStyles) secondLineStyle else firstLineStyle,
		)
		Text(
			modifier = Modifier
				.padding(horizontal = viewMarginLarge, vertical = viewMarginNormal),
				//.fillMaxWidth(1f),
			text = secondLineString,
			style = if (flipLineStyles) firstLineStyle else secondLineStyle,
		)
	}
}
