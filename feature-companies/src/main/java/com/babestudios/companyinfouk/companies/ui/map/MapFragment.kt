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
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.withState
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.companies.ui.CompaniesViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : BaseMvRxFragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

	private var mMap: GoogleMap? = null

	private var location: LatLng? = null

	private val viewModel by existingViewModel(CompaniesViewModel::class)

	var toolBar: ActionBar? = null

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {
		//requireActivity().onBackPressedDispatcher.addCallback(this, callback)
		return inflater.inflate(R.layout.fragment_map, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(tbMap)
		toolBar = (activity as AppCompatActivity).supportActionBar
		toolBar?.setDisplayHomeAsUpEnabled(true)
		withState(viewModel) { state ->
			toolBar?.setTitle(state.companyName)
			location = getLocationFromAddress(state.addressString)
			tbMap.setNavigationOnClickListener { viewModel.companiesNavigator.popBackStack() }
			val mapFragment = requireActivity().supportFragmentManager
					.findFragmentById(R.id.map) as SupportMapFragment?
			mapFragment?.getMapAsync(this)
		}
	}

	override fun onMapReady(googleMap: GoogleMap) {
		mMap = googleMap
		mMap?.setOnMarkerClickListener(this)
		location?.also {
			setLocationInMap(it)
		} ?: run {
			location = LatLng(51.5033635, -0.1276248)
			toolBar?.title = "Could not find address!"
			setLocationInMap(location!!)
		}
	}

	private fun setLocationInMap(location: LatLng) {
		mMap?.uiSettings?.isMapToolbarEnabled = true
		val cameraPosition = CameraPosition.Builder().target(location).zoom(15f).build()
		mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
		withState(viewModel) {
			mMap?.addMarker(MarkerOptions().position(location).title(it.addressString))
		}
		mMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
	}

	private fun getLocationFromAddress(strAddress: String): LatLng? {
		val coder = Geocoder(requireContext())
		val address: List<Address>?
		try {
			address = coder.getFromLocationName(strAddress, 5)
			if (address == null) {
				return null
			}
			val location = address[0]
			return LatLng(location.latitude, location.longitude)
		} catch (e: Exception) {
			Log.e("Map", e.localizedMessage, e)
			return null
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

	//TODO
	/*override fun onBackPressed() {
		super.onBackPressed()
		overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out)
	}*/

	override fun invalidate() {

	}
}
