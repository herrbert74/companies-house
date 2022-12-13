package com.babestudios.companyinfouk.charges.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.babestudios.companyinfouk.charges.R
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.domain.model.charges.Transaction

@Composable
internal fun TransactionListItem(
	item: Transaction,
) {

	val viewMarginNormal = dimensionResource(R.dimen.viewMargin)
	val viewMarginLarge = dimensionResource(R.dimen.viewMarginLarge)

	Column(
		modifier = Modifier
			.fillMaxHeight()
			.fillMaxWidth(1f)
	) {

		Spacer(modifier = Modifier.height(viewMarginNormal))

		Text(
			text = AnnotatedString(item.filingType),
			modifier = Modifier
				.align(Alignment.Start)
				.padding(start = viewMarginLarge),
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			style = CompaniesTypography.titleSmall
		)

		Spacer(modifier = Modifier.height(viewMarginNormal))

		Text(
			text = AnnotatedString(item.deliveredOn),
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

interface ScrollbarAdapter

@Suppress("UNUSED_PARAMETER")
@Composable
fun rememberScrollbarAdapter(
	scrollState: LazyListState,
	itemCount: Int,
	averageItemSize: Dp
): ScrollbarAdapter =
	object : ScrollbarAdapter {}

@Suppress("UNUSED_PARAMETER")
@Composable
fun VerticalScrollbar(
	modifier: Modifier,
	adapter: ScrollbarAdapter
) {
	//no-op
}

@Preview("Item Preview")
@Composable
fun DefaultPreview() {
	TransactionListItem(
		Transaction(
			deliveredOn = "2012-03-12",
			filingType = "Registration of a charge (MR01)"
		),
	)
}
