package com.babestudios.companyinfouk.common.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.babestudios.companyinfouk.common.R
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.SEMI_TRANSPARENT
import com.babestudios.companyinfouk.domain.HALF
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.CollapsingToolbarScaffoldScope
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun HeaderCollapsingToolbarScaffold(
	@DrawableRes headerBackgroundResource: Int,
	navigationAction: () -> Unit,
	topAppBarActions: @Composable (RowScope.() -> Unit),
	title: String,
	body: @Composable (CollapsingToolbarScaffoldScope.() -> Unit),
) {

	val appBarHeight = dimensionResource(R.dimen.appBarHeight)
	val viewMargin = dimensionResource(R.dimen.viewMargin)
	val viewMarginLarge = dimensionResource(R.dimen.viewMarginLarge)

	val state = rememberCollapsingToolbarScaffoldState()
	val progress = state.toolbarState.progress
	val bgColor = MaterialTheme.colorScheme.primary
	val topAppBarColors = TopAppBarDefaults.topAppBarColors(
		containerColor = Color(bgColor.red, bgColor.green, bgColor.blue, 1 - progress),
	)

	CollapsingToolbarScaffold(
		state = state,
		scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
		modifier = Modifier.fillMaxSize(),
		toolbar = {
			Image(
				painter = painterResource(headerBackgroundResource),
				modifier = Modifier
					.parallax(HALF)
					.fillMaxWidth(1f)
					.height(appBarHeight)
					.graphicsLayer {
						// change alpha of Image as the toolbar expands
						alpha = state.toolbarState.progress
					},
				contentScale = ContentScale.Crop,
				contentDescription = null,
				colorFilter = ColorFilter.tint(Color(SEMI_TRANSPARENT), BlendMode.Darken)
			)
			TopAppBar(
				colors = topAppBarColors,
				title = { },
				navigationIcon = {
					IconButton(onClick = navigationAction) {
						Icon(
							imageVector = Icons.Filled.ArrowBack,
							contentDescription = "Finish",
							tint = MaterialTheme.colorScheme.onPrimary
						)
					}
				},
				actions = topAppBarActions,
			)
			Text(
				text = title,
				modifier = Modifier
					.padding(
						start = viewMarginLarge +
							((1 - progress) * 2f * viewMarginLarge.value).dp +
							((1 - progress) * viewMargin.value).dp,
						top = viewMarginLarge,
						bottom = viewMarginLarge,
					)
					.road(
						whenCollapsed = Alignment.CenterStart,
						whenExpanded = Alignment.BottomStart
					),
				style = CompaniesTypography.titleLarge.merge(TextStyle(color = Color.White))
			)
		},
	) {
		body()
	}
}
