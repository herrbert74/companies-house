package com.babestudios.companyinfouk.companies.ui.map

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.babestudios.companyinfouk.design.Colors
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.shared.screen.map.MapComp
import com.babestudios.companyinfouk.shared.screen.map.MapComponent
import com.diamondedge.logging.logging
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val DEFAULT_LATITUDE = 51.5

private const val DEFAULT_LONGITUDE = -0.12

private const val DEFAULT_ZOOM = 12f

private val log = logging()

private val defaultLatLng = LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)

@Composable
@Suppress("LongMethod", "ComplexMethod")
fun MapScreen(component: MapComp) {

	var location by remember { mutableStateOf<LatLng?>(null) }

	val coroutineScope = rememberCoroutineScope()

	val cameraPositionState = rememberCameraPositionState {
		if (location != null)
			position = CameraPosition.fromLatLngZoom(location!!, DEFAULT_ZOOM)
	}

	val uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = true)) }

	val topAppBarColors = TopAppBarDefaults.topAppBarColors(
		containerColor = Colors.primaryContainer,
	)

	val context = LocalContext.current

	LaunchedEffect(coroutineScope) {

		coroutineScope.launch {
			location = getLocationFromAddress(component.address, context)
			cameraPositionState.position = CameraPosition.fromLatLngZoom(location!!, DEFAULT_ZOOM)
		}
	}

	BackHandler(onBack = { component.onBackClicked() })

	Scaffold(
		topBar = {
			TopAppBar(
				colors = topAppBarColors,
				title = { Text(component.name) },
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
				state = rememberUpdatedMarkerState(position = location ?: defaultLatLng),
				title = component.name,
				snippet = component.address
			)
		}
	}

}

@Suppress("ReturnCount", "TooGenericExceptionCaught")
private suspend fun getLocationFromAddress(strAddress: String, context: Context): LatLng =
	suspendCoroutine { continuation ->
		val coder = Geocoder(context)
		lateinit var latLng: LatLng
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			coder.getFromLocationName(strAddress, 1) { addresses ->
				val location = addresses[0]
				latLng = LatLng(location.latitude, location.longitude)
				continuation.resume(latLng)
			}
		} else {
			try {
				@Suppress("DEPRECATION")
				val addresses: List<Address>? = coder.getFromLocationName(strAddress, 1)
				val address = addresses?.getOrNull(0)
				latLng = if (address == null) defaultLatLng else LatLng(address.latitude, address.longitude)
				continuation.resume(latLng)
			} catch (exception: Exception) {
				when (exception) {
					is IOException, is IndexOutOfBoundsException -> {
						log.e(exception) { exception.localizedMessage }
						latLng = defaultLatLng
						continuation.resume(latLng)
					}

					else -> throw exception
				}

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
