package com.babestudios.companyinfouk.insolvencies.ui.insolvencies

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
import com.babestudios.base.compose.simpleVerticalScrollbar
import com.babestudios.companyinfouk.common.compose.CollapsingToolbarScaffold
import com.babestudios.companyinfouk.insolvencies.R
import com.babestudios.companyinfouk.shared.domain.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.shared.screen.insolvencies.InsolvenciesComp

@Composable
fun InsolvenciesScreen(component: InsolvenciesComp) {

	val model by component.state.subscribeAsState()

	BackHandler(onBack = { component.onBackClicked() })

	CollapsingToolbarScaffold(
		backgroundDrawable = R.drawable.bg_insolvency,
		title = stringResource(com.babestudios.companyinfouk.common.R.string.insolvency),
		onBackClicked = { component.onBackClicked() },
		actions = {},
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
					.background(color = Color.Red).padding(paddingValues)
			)
		} else {
			InsolvenciesList(
				items = model.insolvency.cases,
				paddingValues = paddingValues,
				onItemClicked = component::onInsolvencyCaseClicked,
			)
		}
	}

}

@Composable
private fun InsolvenciesList(
	items: List<InsolvencyCase>,
	paddingValues: PaddingValues,
	onItemClicked: (id: InsolvencyCase) -> Unit,
) {

	Box(modifier = Modifier.padding(paddingValues)) {

		val listState = rememberLazyListState()

		LazyColumn(
			Modifier.simpleVerticalScrollbar(listState),
			state = listState
		) {
			items(
				items = items,
				key = { item -> item.hashCode() },
			) { insolvencyCase ->
				InsolvenciesListItem(
					item = insolvencyCase,
					onItemClicked = onItemClicked,
				)

				HorizontalDivider()
			}
		}

	}

}
