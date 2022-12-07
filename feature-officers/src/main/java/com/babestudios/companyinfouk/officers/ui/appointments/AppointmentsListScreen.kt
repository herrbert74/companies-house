@file:Suppress("UNUSED_PARAMETER, FunctionNaming")

package com.babestudios.companyinfouk.officers.ui.appointments

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.babestudios.companyinfouk.common.compose.InfiniteListHandler
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.domain.model.officers.Appointment
import com.babestudios.companyinfouk.officers2.R

@Composable
fun AppointmentsListScreen(component: AppointmentsListComp) {

	val viewMarginNormal = dimensionResource(R.dimen.viewMargin)
	val viewMarginLarge = dimensionResource(R.dimen.viewMarginLarge)

	val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
	BackHandler(onBack = { component.onBackClicked() })
	val model by component.state.subscribeAsState()
	Scaffold(
		modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
		topBar = {
			LargeTopAppBar(
				title = {
					Text(
						text = stringResource(R.string.officer_appointments_title),
						style = CompaniesTypography.titleLarge,
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
				//app:imageViewSrc="@drawable/bg_Appointments"
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
				Column(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
					Text(
						modifier = Modifier
							.padding(start = viewMarginLarge, top = viewMarginNormal, bottom = viewMarginNormal),
						text = model.selectedOfficer.name,
						style = CompaniesTypography.titleLarge,
					)
					AppointmentsList(
						items = model.appointmentsResponse.items,
						onItemClicked = component::onItemClicked,
						onLoadMore = component::onLoadMore,
					)
				}
			}
		})

}

@Composable
private fun AppointmentsList(
	items: List<Appointment>,
	onItemClicked: (id: Appointment) -> Unit,
	onLoadMore: () -> Unit,
) {

	val viewMarginNormal = dimensionResource(R.dimen.viewMargin)

	Box {
		val listState = rememberLazyListState()

		LazyColumn(
			contentPadding = PaddingValues(top = viewMarginNormal),
			state = listState
		) {
			itemsIndexed(items) { _, Appointment ->
				AppointmentListItem(
					item = Appointment,
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
