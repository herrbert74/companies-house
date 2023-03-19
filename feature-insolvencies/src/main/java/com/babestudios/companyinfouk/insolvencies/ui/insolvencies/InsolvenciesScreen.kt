@file:Suppress("UNUSED_PARAMETER, FunctionNaming")

package com.babestudios.companyinfouk.insolvencies.ui.insolvencies

import androidx.activity.compose.BackHandler
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
import com.babestudios.companyinfouk.common.compose.simpleVerticalScrollbar
import com.babestudios.companyinfouk.domain.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.insolvencies.R

@Composable
fun InsolvenciesScreen(component: InsolvenciesComp) {

	val model by component.state.subscribeAsState()

	BackHandler(onBack = { component.onBackClicked() })

	HeaderCollapsingToolbarScaffold(
		headerBackgroundResource = R.drawable.bg_insolvency,
		navigationAction = { component.onBackClicked() },
		topAppBarActions = {},
		title = stringResource(com.babestudios.companyinfouk.common.R.string.insolvency)
	) {
		if (model.isLoading) {
			CircularProgressIndicator()
		} else if (model.error != null) {
			Box(
				Modifier
					.background(color = Color.Red)
			)
		} else {
			InsolvenciesList(
				items = model.insolvency.cases,
				onItemClicked = component::onInsolvencyCaseClicked,
			)
		}
	}

}

@Composable
private fun InsolvenciesList(
	items: List<InsolvencyCase>,
	onItemClicked: (id: InsolvencyCase) -> Unit,
) {

	Box {

		val listState = rememberLazyListState()

		LazyColumn(
			Modifier.simpleVerticalScrollbar(listState),
			state = listState
		) {
			itemsIndexed(items) { _, InsolvencyCase ->
				InsolvenciesListItem(
					item = InsolvencyCase,
					onItemClicked = onItemClicked,
				)

				Divider()
			}
		}

	}

}
