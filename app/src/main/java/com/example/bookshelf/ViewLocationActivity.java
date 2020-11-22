package com.example.bookshelf;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ViewLocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap map; // map item to be used by API
    LatLng location = new LatLng(53.528333, -113.528917); // LatLng type used by the maps API to specify a location
    boolean needsInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment);

        // Getting the LatLng location that was passed via intent
        //location = getIntent().getParcelableExtra("LATITUDE AND LONGITUDE");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (savedInstanceState == null) {
            needsInit=true;
        }

        mapFragment.setRetainInstance(true);
        mapFragment.getMapAsync(this);

        //BOTTOM NAVIGATION_________________________________________________________________________
        //Initialize nav bar and assign it
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);

        // Set home selected
        bottomNavigationView.setSelectedItemId(R.id.maps_page);

        //Item Selected Listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //Pass User's UID into activities on menu click
                switch (menuItem.getItemId()){
                    case R.id.profile_page:
                        Intent profileIntent = new Intent(getApplicationContext(), UserProfileActivity.class);
                        startActivity(profileIntent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.notifications_page:
                        Intent notificationIntent = new Intent(getApplicationContext(), UserNotificationsActivity.class);
                        startActivity(notificationIntent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.books_page:
                        Intent booksIntent = new Intent(getApplicationContext(), UserBooksActivity.class);
                        startActivity(booksIntent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.search_page:
                        Intent searchIntent = new Intent(getApplicationContext(), SearchBooksActivity.class);
                        startActivity(searchIntent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.maps_page:
                        return true;
                }
                return false;
            }
        });


        //__________________________________________________________________________________________
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onMarkerDragStart(Marker marker) {
        LatLng position=marker.getPosition();

        Log.d(getClass().getSimpleName(), String.format("Drag from %f:%f",
                position.latitude,
                position.longitude));
    }

        @Override
        public void onMarkerDrag(Marker marker) {
            LatLng position=marker.getPosition();

            Log.d(getClass().getSimpleName(),
                    String.format("Dragging to %f:%f", position.latitude,
                            position.longitude));
        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            LatLng finalpos=marker.getPosition();

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
        map = googleMap;
        if (needsInit) {
            CameraUpdate center=
                    CameraUpdateFactory.newLatLngZoom(location, 15);

            map.moveCamera(center);
            Marker marker = map.addMarker(new MarkerOptions().position(location).draggable(true));
            LatLng pos = marker.getPosition();
            marker.setTitle("Requested location: " + pos.latitude + ", " + pos.longitude);

            map.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
            map.setOnInfoWindowClickListener((GoogleMap.OnInfoWindowClickListener) this);
            map.setOnMarkerDragListener(this);
        }
    }
}