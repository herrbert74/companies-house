package com.babestudios.companyinfouk.insolvencies.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.Dimens
import com.babestudios.companyinfouk.shared.domain.model.insolvency.Date

@Composable
internal fun InsolvencyDateListItem(item: Date) {

	val viewMarginLarge = Dimens.marginLarge
	val viewMarginNormal = Dimens.marginNormal

	Column(
		modifier = Modifier
			.fillMaxWidth(1f)
	) {

		Spacer(modifier = Modifier.height(viewMarginNormal))

		Text(
			text = AnnotatedString(item.date),
			modifier = Modifier
				.align(Alignment.Start)
				.padding(start = viewMarginLarge),
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			style = CompaniesTypography.titleMedium
		)

		Spacer(modifier = Modifier.height(viewMarginNormal))

		Text(
			text = AnnotatedString(item.type),
			modifier = Modifier
				.align(Alignment.Start)
				.padding(start = viewMarginLarge),
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			style = CompaniesTypography.bodyMedium
		)

		Spacer(modifier = Modifier.height(viewMarginNormal))
	}
}

@Preview("InsolvencyDateListItem Preview")
@Composable
fun InsolvencyDateListItemPreview() {
	InsolvencyDateListItem(
		Date(
			"2016-05-26",
			"Commencement of winding up"
		)
	)
}
