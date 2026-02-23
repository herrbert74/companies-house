package com.babestudios.companyinfouk.filings.ui.filings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.babestudios.companyinfouk.design.Colors
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.design.Dimens
import com.babestudios.companyinfouk.design.component.BodyMediumText
import com.babestudios.companyinfouk.design.component.TitleMediumText
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistoryItem

@Composable
internal fun FilingHistoryItemListItem(
	item: FilingHistoryItem,
	modifier: Modifier = Modifier,
	onItemClick: (id: FilingHistoryItem) -> Unit,
) {
	val viewMarginNormal = Dimens.marginNormal
	val viewMarginLarge = Dimens.marginLarge

	ConstraintLayout(
		modifier = modifier
			.fillMaxWidth(1f)
			.wrapContentHeight(Alignment.CenterVertically)
			.clickable { onItemClick(item) },
	) {
		val (date, category, description, type) = createRefs()

		TitleMediumText(
			textAlign = TextAlign.Start,
			modifier = Modifier
				.padding(start = viewMarginLarge, top = viewMarginNormal)
				.constrainAs(date) {
					width = Dimension.fillToConstraints
					top.linkTo(parent.top)
					start.linkTo(parent.start)
				},
			text = item.date,
		)
		BodyMediumText(
			modifier = Modifier
				.padding(end = viewMarginLarge, top = viewMarginNormal)
				.constrainAs(category) {
					baseline.linkTo(date.baseline)
					top.linkTo(parent.top)
					end.linkTo(parent.end)
				},
			text = item.category.displayName,
		)
		BodyMediumText(
			modifier = Modifier
				.constrainAs(description) {
					width = Dimension.fillToConstraints
					top.linkTo(date.bottom, viewMarginNormal)
					linkTo(parent.start, parent.end, viewMarginLarge, viewMarginLarge, bias = 0f)
				},
			text = item.description.createAnnotatedStringDescription(),
			maxLines = 3,
		)
		TitleMediumText(
			modifier = Modifier
				.wrapContentHeight(Alignment.CenterVertically)
				.padding(start = viewMarginLarge, bottom = viewMarginNormal, top = viewMarginNormal)
				.constrainAs(type) {
					top.linkTo(description.bottom)
					start.linkTo(parent.start)
					width = Dimension.fillToConstraints
				},
			text = item.type,
		)
	}
}

@Preview("FilingHistoryItem Preview")
@Composable
private fun FilingHistoryItemPreview() {
	CompaniesTheme {
		Box(Modifier.background(color = Colors.background)) {
			FilingHistoryItemListItem(
				FilingHistoryItem(
					date = "2016-01-31",
					category = Category.CATEGORY_CONFIRMATION_STATEMENT,
					type = "AA",
					description = "**Termination of appointment** of Abdul Gafoor Kannathody Kunjumuihhamed as a director" +
						" on " +
						"2020-04-02"
				),
				onItemClick = {}
			)
		}
	}
}

@Preview("FilingHistoryItem Dark Preview", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FilingHistoryItemDarkPreview() {
	CompaniesTheme {
		Box(Modifier.background(color = Colors.background)) {
			FilingHistoryItemListItem(
				FilingHistoryItem(
					date = "2016-01-31",
					category = Category.CATEGORY_CONFIRMATION_STATEMENT,
					type = "AA",
					description = "**Termination of appointment** of Abdul Gafoor Kannathody Kunjumuihhamed as a director" +
						" on " +
						"2020-04-02"
				),
				onItemClick = {}
			)
		}
	}
}

internal fun String.createAnnotatedStringDescription(): AnnotatedString {
	val splits = this.split("**")
	val builder = AnnotatedString.Builder()
	splits.forEachIndexed { index, split ->
		if (index % 2 == 0 && split.isNotEmpty()) builder.append(split)
		if (index % 2 == 1) {
			val boldStyle = SpanStyle(fontWeight = FontWeight.Bold)
			val annotatedString = AnnotatedString(split, boldStyle)
			builder.append(annotatedString)
		}
	}
	return builder.toAnnotatedString()
}
