package com.example.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.gms.maps.model.LatLng;

public class RequestDetailsActivity extends AppCompatActivity {
    Double latval, longval;
    LatLng location; // LatLng type used by the maps API to specify a location
    Button getLocation;
    EditText latitude, longitude;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        // Get all the necessary views
        getLocation = findViewById(R.id.get_location);
        latitude = findViewById(R.id.Lat);
        longitude = findViewById(R.id.Long);

        // Listener for the get location button. Will launch the map fragment activity
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latval = Double.parseDouble(latitude.getText().toString());
                longval = Double.parseDouble(longitude.getText().toString());
                location = new LatLng(latval, longval);

                // put the LatLng item into an intent to be used by the map fragment activity
                Intent viewLocation = new Intent(getApplicationContext(), ViewLocationActivity.class);
                viewLocation.putExtra("LATITUDE AND LONGITUDE", location); // TODO: choose lat/lon from map
                startActivity(viewLocation);
                overridePendingTransition(0, 0);
            }
        });


        //BOTTOM NAVIGATION_________________________________________________________________________
        //Initialize nav bar and assign it
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);


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
                }
                return false;
            }
        });
        //__________________________________________________________________________________________

    }
}