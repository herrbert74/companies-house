package com.babestudios.companyinfouk.ui.map

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.ext.logScreenView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

	private var mMap: GoogleMap? = null

	private lateinit var addressString: String
	internal lateinit var companyName: String
	private var location: LatLng? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_map)
		logScreenView(this.localClassName)
		addressString = intent.getStringExtra("addressString")
		companyName = intent.getStringExtra("companyName")
		location = getLocationFromAddress(addressString)

		if (tbMapActivity != null) {
			setSupportActionBar(tbMapActivity)
			supportActionBar?.setDisplayHomeAsUpEnabled(true)
			supportActionBar?.title = companyName
			tbMapActivity.setNavigationOnClickListener { onBackPressed() }
		}

		val mapFragment = supportFragmentManager
				.findFragmentById(R.id.map) as SupportMapFragment?

		mapFragment?.getMapAsync(this)
	}

	override fun onMapReady(googleMap: GoogleMap) {
		mMap = googleMap
		mMap?.setOnMarkerClickListener(this)
		location?.also {
			setLocationInMap(it)
		} ?: run {
			location = LatLng(51.5033635, -0.1276248)
			supportActionBar?.title = "Could not find address!"
			setLocationInMap(location!!)
		}
	}

	private fun setLocationInMap(location: LatLng) {
		mMap?.uiSettings?.isMapToolbarEnabled = true
		val cameraPosition = CameraPosition.Builder().target(location).zoom(15f).build()
		mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
		mMap?.addMarker(MarkerOptions().position(location).title(addressString))
		mMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
	}

	private fun getLocationFromAddress(strAddress: String): LatLng? {
		val coder = Geocoder(this)
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

	override fun onBackPressed() {
		super.onBackPressed()
		overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}
}
