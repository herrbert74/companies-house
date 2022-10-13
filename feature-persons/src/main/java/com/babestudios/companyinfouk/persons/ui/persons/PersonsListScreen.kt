@file:Suppress("UNUSED_PARAMETER")

package com.babestudios.companyinfouk.persons.ui.persons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.babestudios.companyinfouk.domain.model.persons.Person

@Composable
fun PersonsListScreen(component: PersonsListComp) {

	val model by component.state.subscribeAsState()
	Column {
		TopAppBar(title = { Text(text = "Todo List") })

		when (model) {
			is PersonsStore.State.Loading -> {
				CircularProgressIndicator()
			}
			is PersonsStore.State.Error -> {
				Box(Modifier.weight(1F).background(color = Color.Red))
			}
			else -> {
				Box(Modifier.weight(1F)) {
					PersonsList(
						items = (model as PersonsStore.State.Show).personsResponse.items,
						onItemClicked = component::onItemClicked,
					)
				}
			}
		}
	}

}

@Composable
private fun PersonsList(
	items: List<Person>,
	onItemClicked: (id: Person) -> Unit,
) {
	Box {
		val listState = rememberLazyListState()

		LazyColumn(state = listState) {
			itemsIndexed(items) { _, person ->
				Item(
					item = person,
					onItemClicked = onItemClicked,
				)

				Divider()
			}
		}

		VerticalScrollbar(
			modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
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
	Row(modifier = Modifier.clickable(onClick = { onItemClicked(item) })) {

		Spacer(modifier = Modifier.width(8.dp))

		Text(
			text = AnnotatedString(item.name),
			modifier = Modifier.weight(1F).align(Alignment.CenterVertically),
			maxLines = 1,
			overflow = TextOverflow.Ellipsis
		)

		Spacer(modifier = Modifier.width(8.dp))

		Text(
			text = AnnotatedString(item.kind),
			modifier = Modifier.weight(1F).align(Alignment.CenterVertically),
			maxLines = 1,
			overflow = TextOverflow.Ellipsis
		)

		Spacer(modifier = Modifier.width(MARGIN_SCROLLBAR))
	}
}

val MARGIN_SCROLLBAR: Dp = 8.dp

interface ScrollbarAdapter

@Composable
fun rememberScrollbarAdapter(
	scrollState: LazyListState,
	itemCount: Int,
	averageItemSize: Dp
): ScrollbarAdapter =
	object : ScrollbarAdapter {}

@Composable
fun VerticalScrollbar(
	modifier: Modifier,
	adapter: ScrollbarAdapter
) {
	//no-op
}
