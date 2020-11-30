package com.example.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ViewLocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnInfoWindowClickListener {

    LatLng location = new LatLng(53.528333, -113.528917); // LatLng type used by the maps API to specify a location
    LatLng finalPosition;
    private boolean needsInit = false;
    FusedLocationProviderClient fusedLocationProviderClient ;
    String returnpos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment);

        // Getting the LatLng location that was passed via intent
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (savedInstanceState == null) {
            needsInit = true;
        }

        assert mapFragment != null;
        mapFragment.setRetainInstance(true);
        mapFragment.getMapAsync(this);

        Button chooseLocation = findViewById(R.id.chooseLocation);
        chooseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(ViewLocationActivity.this)
                        .setTitle("Confirm Location")
                        .setMessage("Are you sure you want to use this Location?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent output = new Intent();
                                returnpos = finalPosition.latitude + ":" + finalPosition.longitude;
                                output.putExtra("Specified Location", returnpos);
                                setResult(RESULT_OK, output);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

            }
        });

    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onMarkerDragStart(Marker marker) {
        LatLng position = marker.getPosition();

        Log.d(getClass().getSimpleName(), String.format("Drag from %f:%f",
                position.latitude,
                position.longitude));
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        LatLng position = marker.getPosition();

        Log.d(getClass().getSimpleName(),
                String.format("Dragging to %f:%f", position.latitude,
                        position.longitude));
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng finalpos = marker.getPosition();
        finalPosition = finalpos;

        Log.d(getClass().getSimpleName(), String.format("Dragged to %f:%f",
                finalpos.latitude,
                finalpos.longitude));

        marker.setTitle("Requested location: " + finalpos.latitude + ", " + finalpos.longitude);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // map item to be used by API
        if (needsInit) {
//            if (ActivityCompat
//                    .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Unable to get location due to permissions, please" +
//                        "grant the required permissions and try again", Toast.LENGTH_SHORT).show();
//            }
//            fusedLocationProviderClient.getLastLocation()
//                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location loc) {
//                            // Got last known location. In some rare situations this can be null.
//                            location = new LatLng(loc.getLatitude(), loc.getLatitude()); // LatLng type used by the maps API to specify a location
//                            if (loc == null) {
//                                // Logic to handle location object
//                                location = new LatLng(53.528333, -113.528917);
//                            }
//                        }
//                    });
            CameraUpdate center=
                    CameraUpdateFactory.newLatLngZoom(location, 15);

            googleMap.moveCamera(center);
            Marker marker = googleMap.addMarker(new MarkerOptions().position(location).draggable(true));
            LatLng pos = marker.getPosition();
            marker.setTitle("Requested location: " + pos.latitude + ", " + pos.longitude);

            googleMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
            googleMap.setOnInfoWindowClickListener((GoogleMap.OnInfoWindowClickListener) this);
            googleMap.setOnMarkerDragListener(this);
        }
    }
}