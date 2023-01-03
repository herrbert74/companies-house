@file:Suppress("UNUSED_PARAMETER, FunctionNaming")

package com.babestudios.companyinfouk.persons.ui.persons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.babestudios.companyinfouk.common.compose.HeaderCollapsingToolbarScaffold
import com.babestudios.companyinfouk.common.compose.InfiniteListHandler
import com.babestudios.companyinfouk.common.compose.simpleVerticalScrollbar
import com.babestudios.companyinfouk.domain.model.persons.Person
import com.babestudios.companyinfouk.persons.R

@Composable
fun PersonsListScreen(component: PersonsListComp) {

	val model by component.state.subscribeAsState()

	HeaderCollapsingToolbarScaffold(
		headerBackgroundResource = R.drawable.bg_persons,
		navigationAction = { component.finish() },
		topAppBarActions = {},
		title = stringResource(R.string.filing_history)
	) {
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
					items = (model as PersonsStore.State.Show).personsResponse.items,
					onItemClicked = component::onItemClicked,
					onLoadMore = component::onLoadMore,
				)
			}
		}
	}

}

@Composable
private fun PersonsList(
	items: List<Person>,
	onItemClicked: (id: Person) -> Unit,
	onLoadMore: () -> Unit,
) {

	Box {

		val listState = rememberLazyListState()

		LazyColumn(
			Modifier.simpleVerticalScrollbar(listState),
			state = listState
		) {
			itemsIndexed(items) { _, person ->
				PersonsListItem(
					item = person,
					onItemClicked = onItemClicked,
				)

				Divider()
			}
		}

		InfiniteListHandler(listState = listState) {
			onLoadMore()
		}

	}

}
