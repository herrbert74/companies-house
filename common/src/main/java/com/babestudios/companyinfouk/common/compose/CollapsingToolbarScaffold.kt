package com.babestudios.companyinfouk.common.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TwoRowsTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.babestudios.companyinfouk.design.Colors
import com.babestudios.companyinfouk.design.Dimens
import com.babestudios.companyinfouk.design.SEMI_TRANSPARENT
import kotlin.math.min

@Composable
fun CollapsingToolbarScaffold(
	@DrawableRes backgroundDrawable: Int,
	title: String,
	onBackClick: () -> Unit,
	modifier: Modifier = Modifier,
	actions: @Composable RowScope.(Float) -> Unit = {},
	body: @Composable ((PaddingValues) -> Unit),
) {

	val appBarHeight = Dimens.appBarHeight
	val statusBarHeight = with(LocalDensity.current) { WindowInsets.statusBars.getTop(this).toDp() }
	val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
	val collapsedFraction = scrollBehavior.state.collapsedFraction
	val progress = min(1f, collapsedFraction)
	val appBarCollapsedHeight = TopAppBarDefaults.MediumAppBarCollapsedHeight

	Scaffold(
		modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
		topBar = {
			Box {
				Image(
					painter = painterResource(backgroundDrawable),
					modifier = Modifier
						.height(
							((appBarHeight).times(1 - progress))
								.plus((appBarCollapsedHeight.plus(statusBarHeight)).times(progress))
						)
						.fillMaxWidth()
						.graphicsLayer {
							// change alpha of Image as the toolbar expands
							alpha = 1 - progress
						},
					contentScale = ContentScale.Crop,
					contentDescription = null,
					colorFilter = ColorFilter.tint(Color(SEMI_TRANSPARENT), BlendMode.Darken)
				)
				TwoRowsTopAppBar(
					colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
					title = { expanded ->
						Text(
							title,
							modifier = Modifier.padding(bottom = if (expanded) Dimens.marginDoubleLarge else 0.dp),
							color = Color(
								min(1f, Colors.onSurface.red + 1 - progress),
								min(1f, Colors.onSurface.green + 1 - progress),
								min(1f, Colors.onSurface.blue + 1 - progress)
							)
						)

					},
					navigationIcon = {
						IconButton(onClick = { onBackClick() }) {
							Icon(
								imageVector = Icons.AutoMirrored.Filled.ArrowBack,
								contentDescription = "Finish",
								tint = Color(
									min(1f, Colors.onSurface.red + 1 - progress),
									min(1f, Colors.onSurface.green + 1 - progress),
									min(1f, Colors.onSurface.blue + 1 - progress)
								)
							)
						}
					},
					actions = {
						actions(progress)
					},
					expandedHeight = appBarHeight - statusBarHeight,
					scrollBehavior = scrollBehavior,
				)
			}
		}
	) { paddingValues ->
		body(paddingValues)
	}
}
