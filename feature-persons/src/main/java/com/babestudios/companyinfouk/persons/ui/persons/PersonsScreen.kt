package com.babestudios.companyinfouk.persons.ui.persons

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.babestudios.base.compose.InfiniteListHandler
import com.babestudios.base.compose.simpleVerticalScrollbar
import com.babestudios.companyinfouk.common.compose.CollapsingToolbarScaffold
import com.babestudios.companyinfouk.persons.R
import com.babestudios.companyinfouk.shared.domain.model.persons.Person
import com.babestudios.companyinfouk.shared.screen.persons.PersonsComp

@Composable
fun PersonsScreen(component: PersonsComp) {

	val model by component.state.subscribeAsState()

	BackHandler(onBack = { component.onBackClicked() })

	CollapsingToolbarScaffold(
		backgroundDrawable = R.drawable.bg_persons,
		onBackClicked = { component.onBackClicked() },
		title = stringResource(com.babestudios.companyinfouk.common.R.string.persons_with_significant_control)
	) { paddingValues ->
		if (model.isLoading) {
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier.fillMaxSize().padding(paddingValues)
			) {
				CircularProgressIndicator()
			}
		} else if (model.error != null) {
			Box(
				Modifier
					.background(color = Color.Red)
					.padding(paddingValues)
			)
		} else {
			val items = model.personsResponse.items
			if (items.isEmpty()) {
				EmptyPersonsList(paddingValues)
			} else {
				PersonsList(
					items = items,
					paddingValues,
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
	paddingValues: PaddingValues,
	onItemClicked: (id: Person) -> Unit,
	onLoadMore: () -> Unit,
) {

	Box(Modifier.padding(paddingValues)) {

		val listState = rememberLazyListState()

		LazyColumn(
			Modifier.simpleVerticalScrollbar(listState),
			state = listState
		) {
			items(
				items = items,
				key = { item -> item.hashCode() },
			) { person ->
				PersonsListItem(
					item = person,
					onItemClicked = onItemClicked,
				)

				HorizontalDivider()
			}
		}

		InfiniteListHandler(listState = listState) {
			onLoadMore()
		}

	}

}
