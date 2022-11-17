@file:Suppress("UNUSED_PARAMETER, FunctionNaming")

package com.babestudios.companyinfouk.persons.ui.persons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.babestudios.companyinfouk.common.compose.InfiniteListHandler
import com.babestudios.companyinfouk.design.CompaniesHouseTypography
import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.domain.model.persons.Person

@Composable
fun PersonsListScreen(component: PersonsListComp) {

	val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
	val model by component.state.subscribeAsState()
	Scaffold(
		modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
		topBar = {
			LargeTopAppBar(
				title = {
					Text(
						text = "Persons with significant control", style = CompaniesHouseTypography.titleLarge
					)
				},
				navigationIcon = {
					IconButton(onClick = { component.finish() }) {
						Icon(
							imageVector = Icons.Filled.ArrowBack,
							contentDescription = "Localized description"
						)
					}
				},
				//Add back image background oce supported
				//app:imageViewSrc="@drawable/bg_persons"
				scrollBehavior = scrollBehavior
			)
		},
		content = { innerPadding ->
			when (model) {
				is PersonsStore.State.Loading -> {
					CircularProgressIndicator()
				}
				is PersonsStore.State.Error -> {
					Box(
						Modifier
							.background(color = Color.Red)
					)
				}
				else -> {
					PersonsList(
						paddingValues = innerPadding,
						items = (model as PersonsStore.State.Show).personsResponse.items,
						onItemClicked = component::onItemClicked,
						onLoadMore = component::onLoadMore,
					)
				}
			}
		})

}

@Composable
private fun PersonsList(
	paddingValues: PaddingValues,
	items: List<Person>,
	onItemClicked: (id: Person) -> Unit,
	onLoadMore: () -> Unit,
) {
	Box {
		val listState = rememberLazyListState()

		LazyColumn(
			contentPadding = paddingValues,
			state = listState
		) {
			itemsIndexed(items) { _, person ->
				Item(
					item = person,
					onItemClicked = onItemClicked,
				)

				Divider()
			}
		}

		InfiniteListHandler(listState = listState) {
			onLoadMore()
		}

		VerticalScrollbar(
			modifier = Modifier
				.align(Alignment.CenterEnd)
				.fillMaxHeight(),
			adapter = rememberScrollbarAdapter(
				scrollState = listState,
				itemCount = items.size,
				averageItemSize = 37.dp
			)
		)
	}
}

@Composable
private fun Item(
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
			style = CompaniesHouseTypography.titleSmall
		)

		Spacer(modifier = Modifier.height(MARGIN_SCROLLBAR))

		Text(
			text = AnnotatedString(item.kind),
			modifier = Modifier
				.align(Alignment.Start)
				.padding(start = MARGIN_SCROLLBAR),
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			style = CompaniesHouseTypography.bodyMedium
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
	Item(
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
