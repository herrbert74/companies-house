@file:Suppress("UNUSED_PARAMETER, FunctionNaming")

package com.babestudios.companyinfouk.insolvencies.ui.insolvencies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.babestudios.companyinfouk.common.compose.simpleVerticalScrollbar
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.domain.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.insolvencies.R

@Composable
fun InsolvenciesListScreen(component: InsolvenciesComp) {

	val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
	val model by component.state.subscribeAsState()
	val title = stringResource(R.string.insolvency)

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
							contentDescription = "Localized description"
						)
					}
				},
				//Add back image background oce supported
				//app:imageViewSrc="@drawable/bg_Insolvencies"
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
				InsolvenciesList(
					paddingValues = innerPadding,
					items = model.insolvency.cases,
					onItemClicked = component::onInsolvencyCaseClicked,
				)
			}
		})

}

@Composable
private fun InsolvenciesList(
	paddingValues: PaddingValues,
	items: List<InsolvencyCase>,
	onItemClicked: (id: InsolvencyCase) -> Unit,
) {

	Box {

		val listState = rememberLazyListState()

		LazyColumn(
			Modifier.simpleVerticalScrollbar(listState),
			contentPadding = paddingValues,
			state = listState
		) {
			itemsIndexed(items) { _, InsolencyCase ->
				InsolvenciesListItem(
					item = InsolencyCase,
					onItemClicked = onItemClicked,
				)

				Divider()
			}
		}

	}

}
