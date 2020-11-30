package com.example.bookshelf;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class BorrowBook extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    LatLng location = new LatLng(53.528333, -113.528917); // LatLng type used by the maps API to specify a location
    boolean needsInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrow_book);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);

        if (savedInstanceState == null) {
            needsInit = true;
        }

        // Uncomment lines 36 & 37 and delete line 38 for the app to grab the location string via intent
//        Intent intent = getIntent();
//        String setlocation = intent.getStringExtra("Specified Location");
        String setlocation = "53.528333:-113.528917";
        String[] separated = setlocation.split(":");
        Double lat = Double.valueOf(separated[0]);
        Double longg = Double.valueOf(separated[1]);

        location = new LatLng(lat, longg);

        assert mapFragment != null;
        mapFragment.setRetainInstance(true);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // map item to be used by API
        if (needsInit) {
            CameraUpdate center =
                    CameraUpdateFactory.newLatLngZoom(location, 15);

            googleMap.moveCamera(center);
            Marker marker = googleMap.addMarker(new MarkerOptions().position(location));
            LatLng pos = marker.getPosition();
            marker.setTitle("Requested location: " + pos.latitude + ", " + pos.longitude);

            googleMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
            googleMap.setOnInfoWindowClickListener((GoogleMap.OnInfoWindowClickListener) this);
        }
    }
}

