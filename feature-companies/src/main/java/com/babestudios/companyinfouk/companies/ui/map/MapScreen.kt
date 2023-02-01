package com.babestudios.companyinfouk.companies.ui.map

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

private val defaultLatLng = LatLng(51.5, -0.12)

@Composable
@Suppress("LongMethod", "ComplexMethod")
fun MapScreen(component: MapComp) {

	val location = getLocationFromAddress(component.address, LocalContext.current)

	val cameraPositionState = rememberCameraPositionState {
		position = CameraPosition.fromLatLngZoom(location, 10f)
	}

	val uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = true)) }

	val topAppBarColors = TopAppBarDefaults.topAppBarColors(
		containerColor = MaterialTheme.colorScheme.primaryContainer,
	)

	BackHandler(onBack = { component.onBackClicked() })

	Scaffold(
		topBar = {
			TopAppBar(
				colors = topAppBarColors,
				title = { Text(component.name) },
				actions = {},
			)
		}
	) { paddingValues ->

		GoogleMap(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues),
			cameraPositionState = cameraPositionState,
			uiSettings = uiSettings,
		) {
			Marker(
				state = MarkerState(position = location),
				title = component.address,
				snippet = "Marker in Singapore"
			)
		}
	}

}

@Suppress("ReturnCount", "TooGenericExceptionCaught")
private fun getLocationFromAddress(strAddress: String, context: Context): LatLng {
	val coder = Geocoder(context)
	val address: List<Address>?
	try {
		address = coder.getFromLocationName(strAddress, 1)
		if (address == null) {
			return defaultLatLng
		}
		val location = address[0]
		return LatLng(location.latitude, location.longitude)
	} catch (e: Exception) {
		when (e) {
			is IOException, is IndexOutOfBoundsException -> {
				Timber.e("Map", e.localizedMessage, e)
				return defaultLatLng
			}

			else -> throw e
		}

	}
}

@Preview("map Preview")
@Composable
fun MapScreenPreview() {
	val componentContext = DefaultComponentContext(lifecycle = LifecycleRegistry())
	CompaniesTheme {
		MapScreen(
			MapComponent(
				componentContext,
				Dispatchers.Main,
				name = "Robert Who",
				address = "10 Downing St, London SW1A 2AB"
			) { }
		)
	}
}
