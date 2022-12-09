@file:Suppress("UNUSED_PARAMETER, FunctionNaming")

package com.babestudios.companyinfouk.officers.ui.officers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.babestudios.companyinfouk.common.compose.InfiniteListHandler
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.domain.model.officers.Officer

@Composable
fun OfficersListScreen(component: OfficersListComp) {

	val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
	val model by component.state.subscribeAsState()
	Scaffold(
		modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
		topBar = {
			LargeTopAppBar(
				title = {
					Text(
						text = "Officers", style = CompaniesTypography.titleLarge
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
				//app:imageViewSrc="@drawable/bg_officers"
				scrollBehavior = scrollBehavior
			)
		},
		content = { innerPadding ->
			if (model.isLoading) {
				CircularProgressIndicator()
			} else if (model.error != null) {
				Box(
					Modifier
						.background(color = Color.Red)
				)
			} else {
				OfficersList(
					paddingValues = innerPadding,
					items = model.officersResponse.items,
					onItemClicked = component::onItemClicked,
					onLoadMore = component::onLoadMore,
				)
			}
		}
	)

}

@Composable
private fun OfficersList(
	paddingValues: PaddingValues,
	items: List<Officer>,
	onItemClicked: (id: Officer) -> Unit,
	onLoadMore: () -> Unit,
) {
	Box {
		val listState = rememberLazyListState()

		LazyColumn(
			contentPadding = paddingValues,
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
