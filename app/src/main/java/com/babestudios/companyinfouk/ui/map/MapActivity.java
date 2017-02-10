package com.babestudios.companyinfouk.ui.map;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pascalwelsch.compositeandroid.activity.CompositeActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapActivity extends CompositeActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

	private GoogleMap mMap;

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	String addressString;
	String companyName;
	LatLng location;

	BaseActivityPlugin baseActivityPlugin = new BaseActivityPlugin();

	public MapActivity() {
		addPlugin(baseActivityPlugin);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		baseActivityPlugin.logScreenView(this.getLocalClassName());

		addressString = getIntent().getStringExtra("addressString");
		companyName = getIntent().getStringExtra("companyName");
		location = getLocationFromAddress(addressString);

		ButterKnife.bind(this);

		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(companyName);
			toolbar.setNavigationOnClickListener(v -> onBackPressed());
		}

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);

		mapFragment.getMapAsync(this);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		mMap.setOnMarkerClickListener(this);
		if (location != null) {
			setLocationInMap(location);
		} else {
			location = new LatLng(51.5033635, -0.1276248);
			getSupportActionBar().setTitle("Could not find address!");
			setLocationInMap(location);
		}
	}

	private void setLocationInMap(LatLng location) {
		mMap.getUiSettings().setMapToolbarEnabled(true);
		CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(15).build();
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		mMap.addMarker(new MarkerOptions().position(location).title(addressString));
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

	private LatLng getLocationFromAddress(String strAddress) {
		Geocoder coder = new Geocoder(this);
		List<Address> address;
		try {
			address = coder.getFromLocationName(strAddress, 5);
			if (address == null) {
				return null;
			}
			Address location = address.get(0);
			return new LatLng(location.getLatitude(), location.getLongitude());
		} catch (Exception e) {
			Log.e("Map", e.getLocalizedMessage(), e);
			return null;
		}
	}

	@Override
	public boolean onMarkerClick(final Marker marker) {
		if (marker.isInfoWindowShown()) {
			marker.hideInfoWindow();
		} else {
			marker.showInfoWindow();
		}
		return true;
	}

	@Override
	public void super_onBackPressed() {
		super.super_finish();
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
	}
}
