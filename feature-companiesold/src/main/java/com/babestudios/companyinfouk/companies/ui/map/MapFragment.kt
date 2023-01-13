package com.babestudios.companyinfouk.companies.ui.map

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.babestudios.base.ext.viewBinding
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.companies.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import timber.log.Timber

const val STARTING_LATITUDE: Double = 51.5033635
const val STARTING_LONGITUDE: Double = -0.1276248
const val STARTING_ZOOM = 15f

//@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

	private val args: MapFragmentArgs by navArgs()

	private var googleMap: GoogleMap? = null

	private var location: LatLng? = null

	private var toolBar: ActionBar? = null

	private val binding by viewBinding<FragmentMapBinding>()

//	@Inject
//	lateinit var companiesRepository: CompaniesRepository

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {
		//companiesRepository.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(binding.tbMap)
		toolBar = (activity as AppCompatActivity).supportActionBar
		toolBar?.setDisplayHomeAsUpEnabled(true)
		toolBar?.title = args.name
		location = getLocationFromAddress(args.address)
		binding.tbMap.setNavigationOnClickListener {
			activity?.onBackPressed()
		}
		val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
		mapFragment?.getMapAsync(this)
	}

	override fun onMapReady(map: GoogleMap) {
		googleMap = map
		googleMap?.setOnMarkerClickListener(this)
		googleMap?.uiSettings?.isZoomControlsEnabled = true
		location?.also {
			setLocationInMap(it)
		} ?: run {
			location = LatLng(STARTING_LATITUDE, STARTING_LONGITUDE)
			toolBar?.title = "Could not find address!"
			setLocationInMap(location!!)
		}
	}

	private fun setLocationInMap(location: LatLng) {
		googleMap?.uiSettings?.isMapToolbarEnabled = true
		val cameraPosition = CameraPosition.Builder().target(location).zoom(STARTING_ZOOM).build()
		googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
		googleMap?.addMarker(
			MarkerOptions().position(location).title(args.address)
		)
		googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
	}

	@Suppress("ReturnCount", "TooGenericExceptionCaught")
	private fun getLocationFromAddress(strAddress: String): LatLng? {
		val coder = Geocoder(requireContext())
		val address: List<Address>?
		try {
			address = coder.getFromLocationName(strAddress, 1)
			if (address == null) {
				return null
			}
			val location = address[0]
			return LatLng(location.latitude, location.longitude)
		} catch (e: Exception) {
			when (e) {
				is IOException, is IndexOutOfBoundsException -> {
					Timber.e("Map", e.localizedMessage, e)
					return null
				}
				else -> throw e
			}

		}
	}

	override fun onMarkerClick(marker: Marker): Boolean {
		if (marker.isInfoWindowShown) {
			marker.hideInfoWindow()
		} else {
			marker.showInfoWindow()
		}
		return true
	}

}
