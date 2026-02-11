package com.babestudios.companyinfouk.design.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.babestudios.companyinfouk.design.Colors
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.titleLargeBold

@Composable
fun TitleLargeBoldText(
	text: String,
	modifier: Modifier = Modifier,
	maxLines: Int = 3,
	textAlign: TextAlign = TextAlign.Start,
	overflow: TextOverflow = TextOverflow.Ellipsis,
) {
	Text(
		modifier = modifier,
		text = text,
		style = CompaniesTypography.titleLargeBold.merge(Colors.onBackground),
		textAlign = textAlign,
		maxLines = maxLines,
		overflow = overflow
	)
}

@Composable
fun TitleMediumText(
	text: AnnotatedString,
	modifier: Modifier = Modifier,
	maxLines: Int = 3,
	textAlign: TextAlign = TextAlign.Start,
	overflow: TextOverflow = TextOverflow.Ellipsis,
) {
	Text(
		modifier = modifier,
		text = text,
		style = CompaniesTypography.titleMedium.merge(Colors.onBackground),
		textAlign = textAlign,
		maxLines = maxLines,
		overflow = overflow
	)
}

@Composable
fun TitleMediumText(
	text: String,
	modifier: Modifier = Modifier,
	maxLines: Int = 3,
	textAlign: TextAlign = TextAlign.Start,
	overflow: TextOverflow = TextOverflow.Ellipsis,
) {
	Text(
		modifier = modifier,
		text = text,
		style = CompaniesTypography.titleMedium.merge(Colors.onBackground),
		textAlign = textAlign,
		maxLines = maxLines,
		overflow = overflow
	)
}

@Composable
fun TitleSmallText(
	text: AnnotatedString,
	modifier: Modifier = Modifier,
	maxLines: Int = 3,
	textAlign: TextAlign = TextAlign.Start,
	overflow: TextOverflow = TextOverflow.Ellipsis,
) {
	Text(
		modifier = modifier,
		text = text,
		style = CompaniesTypography.titleSmall.merge(Colors.onBackground),
		textAlign = textAlign,
		maxLines = maxLines,
		overflow = overflow
	)
}

@Composable
fun TitleSmallText(
	text: String,
	modifier: Modifier = Modifier,
	maxLines: Int = 3,
	textAlign: TextAlign = TextAlign.Start,
	overflow: TextOverflow = TextOverflow.Ellipsis,
) {
	Text(
		modifier = modifier,
		text = text,
		style = CompaniesTypography.titleSmall.merge(Colors.onBackground),
		textAlign = textAlign,
		maxLines = maxLines,
		overflow = overflow
	)
}

@Composable
fun BodyMediumText(
	text: AnnotatedString,
	modifier: Modifier = Modifier,
	maxLines: Int = 3,
	textAlign: TextAlign = TextAlign.Start,
	overflow: TextOverflow = TextOverflow.Ellipsis,
) {
	Text(
		modifier = modifier,
		text = text,
		style = CompaniesTypography.bodyMedium.merge(Colors.onBackground),
		textAlign = textAlign,
		maxLines = maxLines,
		overflow = overflow
	)
}

@Composable
fun BodyMediumText(
	text: String,
	modifier: Modifier = Modifier,
	maxLines: Int = 3,
	textAlign: TextAlign = TextAlign.Start,
	overflow: TextOverflow = TextOverflow.Ellipsis,
) {
	Text(
		modifier = modifier,
		text = text,
		style = CompaniesTypography.bodyMedium.merge(Colors.onBackground),
		textAlign = textAlign,
		maxLines = maxLines,
		overflow = overflow
	)
}
