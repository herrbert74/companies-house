package com.babestudios.companyinfouk.companies.ui.map

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.withState
import com.babestudios.base.mvrx.BaseFragment
import com.babestudios.companyinfouk.common.model.common.getAddressString
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.companies.databinding.FragmentMapBinding
import com.babestudios.companyinfouk.companies.ui.CompaniesActivity
import com.babestudios.companyinfouk.companies.ui.CompaniesViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

const val STARTING_LATITUDE: Double = 51.5033635
const val STARTING_LONGITUDE: Double = -0.1276248
const val STARTING_ZOOM = 15f

class MapFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

	private var googleMap: GoogleMap? = null

	private var location: LatLng? = null

	private val viewModel by existingViewModel(CompaniesViewModel::class)

	private var toolBar: ActionBar? = null

	private var _binding: FragmentMapBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {
		_binding = FragmentMapBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	override fun orientationChanged() {
		val activity = requireActivity() as CompaniesActivity
		viewModel.setNavigator(activity.injectCompaniesNavigator())
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(binding.tbMap)
		toolBar = (activity as AppCompatActivity).supportActionBar
		toolBar?.setDisplayHomeAsUpEnabled(true)
		withState(viewModel) { state ->
			toolBar?.title = if (state.companyName.isEmpty()) state.individualName else state.companyName
			location = if (state.individualAddress != null)
				getLocationFromAddress(state.individualAddress.getAddressString())
			else
				getLocationFromAddress(state.company.registeredOfficeAddress.getAddressString())
			binding.tbMap.setNavigationOnClickListener {
				activity?.onBackPressed()
			}
			val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
			mapFragment?.getMapAsync(this)
		}
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
		withState(viewModel) {
			googleMap?.addMarker(MarkerOptions().position(location).title(it.company.registeredOfficeAddress
					.getAddressString()))
		}
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
					Log.e("Map", e.localizedMessage, e)
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

	@Suppress("EmptyFunctionBlock")
	override fun invalidate() {

	}
}
