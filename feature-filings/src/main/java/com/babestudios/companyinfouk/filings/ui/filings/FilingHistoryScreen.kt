@file:Suppress("UNUSED_PARAMETER, FunctionNaming")

package com.babestudios.companyinfouk.filings.ui.filings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.babestudios.companyinfouk.common.compose.InfiniteListHandler
import com.babestudios.companyinfouk.common.compose.simpleVerticalScrollbar
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.filings.R

@Composable
fun FilingHistoryScreen(component: FilingHistoryComp) {

	val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
	val model by component.state.subscribeAsState()
	val title = stringResource(R.string.filing_history)
	val categories = stringArrayResource(R.array.filing_history_categories)
	val bodyContent = rememberSaveable { mutableStateOf(categories.first()) }

	Scaffold(
		modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
		topBar = {
			LargeTopAppBar(
				title = {
					Text(
						text = title, style = CompaniesTypography.titleLarge
					)
				},
				navigationIcon = {
					IconButton(onClick = { component.onBackClicked() }) {
						Icon(
							imageVector = Icons.Filled.ArrowBack,
							contentDescription = "Finish"
						)
					}
				},
				actions = {
					Text(bodyContent.value)
					CategoryFilterDropdown(categories = categories.toList(), bodyContent = bodyContent, component)
				},
				//Add back image background oce supported
				//app:imageViewSrc="@drawable/bg_FilingHistory"
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
				FilingHistoryList(
					paddingValues = innerPadding,
					items = model.filingHistory.items,
					onItemClicked = component::onItemClicked,
					onLoadMore = component::onLoadMore,
				)
			}
		})

}

@Composable
private fun FilingHistoryList(
	paddingValues: PaddingValues,
	items: List<FilingHistoryItem>,
	onItemClicked: (id: FilingHistoryItem) -> Unit,
	onLoadMore: () -> Unit,
) {

	Box {

		val listState = rememberLazyListState()

		LazyColumn(
			Modifier.simpleVerticalScrollbar(listState),
			contentPadding = paddingValues,
			state = listState
		) {
			itemsIndexed(items) { _, FilingHistoryItem ->
				FilingHistoryItemListItem(
					item = FilingHistoryItem,
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

@Composable
fun CategoryFilterDropdown(categories: List<String>, bodyContent: MutableState<String>, component: FilingHistoryComp) {
	val expanded = remember { mutableStateOf(false) }
	var selectedIndex by remember { mutableStateOf(0) }

	Box(
		Modifier
			.wrapContentSize(Alignment.TopEnd)
	) {
		IconButton(onClick = {
			expanded.value = true
		}) {
			Icon(
				Icons.Filled.ArrowDropDown,
				contentDescription = "More Menu"
			)
		}
	}

	DropdownMenu(
		expanded = expanded.value,
		onDismissRequest = { expanded.value = false },
		modifier = Modifier
			.wrapContentWidth()
			.background(Color.LightGray)
	) {
		categories.forEachIndexed { index, category ->
			DropdownMenuItem(onClick = {
				selectedIndex = index
				expanded.value = false
				bodyContent.value = category
				component.onFilingHistoryCategorySelected(index)
			}) {
				Text(text = category)
			}
		}
	}
}
