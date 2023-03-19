@file:Suppress("UNUSED_PARAMETER, FunctionNaming")

package com.babestudios.companyinfouk.officers.ui.officers

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
import com.babestudios.companyinfouk.common.compose.InfiniteListHandler
import com.babestudios.companyinfouk.common.compose.simpleVerticalScrollbar
import com.babestudios.companyinfouk.domain.model.officers.Officer
import com.babestudios.companyinfouk.officers.R

@Composable
fun OfficersScreen(component: OfficersComp) {

	val model by component.state.subscribeAsState()

	BackHandler(onBack = { component.onBackClicked() })

	HeaderCollapsingToolbarScaffold(
		headerBackgroundResource = R.drawable.bg_officers,
		navigationAction = { component.onBackClicked() },
		topAppBarActions = {},
		title = stringResource(com.babestudios.companyinfouk.common.R.string.officers)
	) {
		if (model.isLoading) {
			CircularProgressIndicator()
		} else if (model.error != null) {
			Box(
				Modifier
					.background(color = Color.Red)
			)
		} else {
			OfficersList(
				items = model.officersResponse.items,
				onItemClicked = component::onItemClicked,
				onLoadMore = component::onLoadMore,
			)
		}
	}

}

@Composable
private fun OfficersList(
	items: List<Officer>,
	onItemClicked: (id: Officer) -> Unit,
	onLoadMore: () -> Unit,
) {

	Box {

		val listState = rememberLazyListState()

		LazyColumn(
			Modifier.simpleVerticalScrollbar(listState),
			state = listState
		) {
			itemsIndexed(items) { _, Officer ->
				OfficerListItem(
					item = Officer,
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
