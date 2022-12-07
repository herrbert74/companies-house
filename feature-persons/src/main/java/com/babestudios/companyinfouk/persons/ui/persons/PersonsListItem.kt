package com.babestudios.companyinfouk.persons.ui.persons

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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.domain.model.persons.Person


@Composable
internal fun PersonsListItem(
	item: Person,
	onItemClicked: (id: Person) -> Unit,
) {
	Column(
		modifier = Modifier
			.fillMaxHeight()
			.fillMaxWidth(1f)
			.clickable { onItemClicked(item) }
	) {

		Spacer(modifier = Modifier.height(MARGIN_SCROLLBAR))

		Text(
			text = AnnotatedString(item.name),
			modifier = Modifier
				.align(Alignment.Start)
				.padding(start = MARGIN_SCROLLBAR),
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			style = CompaniesTypography.titleSmall
		)

		Spacer(modifier = Modifier.height(MARGIN_SCROLLBAR))

		Text(
			text = AnnotatedString(item.kind),
			modifier = Modifier
				.align(Alignment.Start)
				.padding(start = MARGIN_SCROLLBAR),
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			style = CompaniesTypography.bodyMedium
		)

		Spacer(modifier = Modifier.height(MARGIN_SCROLLBAR))
	}
}

val MARGIN_SCROLLBAR: Dp = 8.dp

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
	PersonsListItem(
		Person(
			name = "Robert Who",
			notifiedOn = "",
			address = Address(),
			kind = "Individual",
			naturesOfControl = listOf("individual")
		),
		onItemClicked = {}
	)
}
