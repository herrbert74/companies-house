@file:Suppress("UNUSED_PARAMETER, FunctionNaming")

package com.babestudios.companyinfouk.officers.ui.appointments

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.babestudios.base.compose.InfiniteListHandler
import com.babestudios.base.compose.simpleVerticalScrollbar
import com.babestudios.companyinfouk.common.compose.HeaderCollapsingToolbarScaffold
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.Dimens
import com.babestudios.companyinfouk.design.titleLargeBold
import com.babestudios.companyinfouk.shared.domain.model.officers.Appointment
import com.babestudios.companyinfouk.officers.R

@Composable
fun AppointmentsScreen(component: AppointmentsComp) {

	val viewMarginLarge = Dimens.marginLarge
	val viewMarginNormal = Dimens.marginNormal

	BackHandler(onBack = { component.onBackClicked() })
	val model by component.state.subscribeAsState()

	HeaderCollapsingToolbarScaffold(
		headerBackgroundResource = R.drawable.bg_officers,
		navigationAction = { component.onBackClicked() },
		topAppBarActions = {},
		title = stringResource(R.string.officer_appointments_title)
	) {
		if (model.isLoading) {
			CircularProgressIndicator()
		} else if (model.error != null) {
			Box(Modifier.background(color = Color.Red))
		} else {
			Column {
				Text(
					modifier = Modifier
						.padding(start = viewMarginLarge, top = viewMarginNormal, bottom = viewMarginNormal),
					text = model.selectedOfficer.name,
					style = CompaniesTypography.titleLargeBold,
				)
				AppointmentsList(
					items = model.appointmentsResponse.items,
					onItemClicked = component::onItemClicked,
					onLoadMore = component::onLoadMore,
				)
			}
		}
	}

}

@Composable
private fun AppointmentsList(
	items: List<Appointment>,
	onItemClicked: (id: Appointment) -> Unit,
	onLoadMore: () -> Unit,
) {

	val viewMarginNormal = Dimens.marginNormal

	Box {

		val listState = rememberLazyListState()

		LazyColumn(
			Modifier.simpleVerticalScrollbar(listState),
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

	}

}
