package com.swdm.cc;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapView extends FragmentActivity {
	String address;
	String snipp;
	String title;
	double lat; // latitude
	double lng; // longitude
	GoogleMap mGoogleMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_map);

		// calculate lat and lng
		this.setData();
	}

	public void setData() {
		Intent intent = getIntent();
		address = intent.getExtras().get("address").toString();
		title = intent.getExtras().get("name").toString();
		snipp = intent.getExtras().get("tags").toString();

		Geocoder geocoder = new Geocoder(this, Locale.KOREA);

		try {
			List<Address> addrs = geocoder.getFromLocationName(address, 1);

			String locations = "";
			for (Address addr : addrs) {
				for (int i = 0; i <= addr.getMaxAddressLineIndex(); i++) {
					locations += addr.getAddressLine(i) + "\n";
				}

				lat = addr.getLatitude();
				lng = addr.getLongitude();

				/*
				 * Toast.makeText(getApplicationContext(), "latitude : " + lat +
				 * " longitude : " + lng, Toast.LENGTH_SHORT).show();
				 */
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		LatLng position = new LatLng(lat, lng);
		GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(GoogleMapView.this);

		mGoogleMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		// touch event
		// mGoogleMap.setOnMapClickListener((OnMapClickListener) this);

		// change camera
		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

		// setting marker
		MarkerOptions optFirst = new MarkerOptions();
		optFirst.position(position);
		optFirst.title(title); // set title
		optFirst.snippet(snipp); // set snippet = "tag"
		// optFirst.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
		mGoogleMap.addMarker(optFirst).showInfoWindow();

		// marker click listener
		mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			public boolean onMarkerClick(Marker marker) {

				return false;
			}
		});
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.activity_main, menu); return true; }
	 */
}
