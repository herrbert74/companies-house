package com.babestudios.companyinfouk.officers.ui.officers

import androidx.compose.foundation.clickable
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
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.domain.model.officers.Officer
import com.babestudios.companyinfouk.officers2.R

@Composable
internal fun OfficerListItem(
	item: Officer,
	onItemClicked: (id: Officer) -> Unit,
) {

	val viewMarginNormal = dimensionResource(R.dimen.viewMargin)
	val viewMarginLarge = dimensionResource(R.dimen.viewMarginLarge)

	Column(
		modifier = Modifier
			.fillMaxHeight()
			.fillMaxWidth(1f)
			.clickable { onItemClicked(item) }
	) {

		Spacer(modifier = Modifier.height(viewMarginNormal))

		Text(
			text = AnnotatedString(item.name),
			modifier = Modifier
				.align(Alignment.Start)
				.padding(start = viewMarginLarge),
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			style = CompaniesTypography.titleLarge
		)

		Spacer(modifier = Modifier.height(viewMarginNormal))

		Text(
			text = AnnotatedString(item.fromToString),
			modifier = Modifier
				.align(Alignment.Start)
				.padding(start = viewMarginLarge),
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			style = CompaniesTypography.bodyLarge
		)

		Spacer(modifier = Modifier.height(viewMarginNormal))

		Text(
			text = AnnotatedString(item.officerRole),
			modifier = Modifier
				.align(Alignment.Start)
				.padding(start = viewMarginLarge),
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			style = CompaniesTypography.bodyLarge
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
	averageItemSize: Dp,
): ScrollbarAdapter =
	object : ScrollbarAdapter {}

@Suppress("UNUSED_PARAMETER")
@Composable
fun VerticalScrollbar(
	modifier: Modifier,
	adapter: ScrollbarAdapter,
) {
	//no-op
}

@Preview("Item Preview")
@Composable
fun DefaultPreview() {
	OfficerListItem(
		Officer(
			name = "Savory, Fiona Anne, Lady",
			appointmentsId = "",
			address = Address(),
			fromToString = "2012-2-23",
			officerRole = "Director"
		),
		onItemClicked = {}
	)
}
