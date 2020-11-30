package com.example.bookshelf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.gms.maps.model.LatLng;

public class RequestDetailsActivity extends AppCompatActivity {
    Double latval, longval;
    LatLng location; // LatLng type used by the maps API to specify a location
    Button getLocation, saveLocation;
    TextView latlng, title;
    final int GET_LOCATION = 1;
    boolean ownerFlag;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == GET_LOCATION) && resultCode == RESULT_OK){
            assert data != null;
            location = data.getParcelableExtra("Specified Location");
            latlng.setText(String.format("Latitude: %s and Longitude: %s", location.latitude, location.longitude));
        }

        if (location == null)
        {
            location = new LatLng(53.528333, -113.528917);
        }
    }

    public void transformActivity() {
        title = findViewById(R.id.req_text);
        saveLocation = findViewById(R.id.save_details);
        getLocation = findViewById(R.id.get_location);

        if (ownerFlag) {
            saveLocation.setVisibility(View.INVISIBLE);
            title.setText("");
            title.setText("View Request Details");
            getLocation.setText("");
            getLocation.setText("View Location");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        ownerFlag = intent.getBooleanExtra("Owner Flag", false);
        transformActivity();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        // Get all the necessary views
        getLocation = findViewById(R.id.get_location);
        latlng = findViewById(R.id.Lat);





        // Listener for the get location button. Will launch the map fragment activity
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // put the LatLng item into an intent to be used by the map fragment activity
                Intent viewLocation = new Intent(getApplicationContext(), ViewLocationActivity.class);
                startActivityForResult(viewLocation, GET_LOCATION);
                overridePendingTransition(0, 0);
            }
        });





        saveLocation = findViewById(R.id.save_details);
        saveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(RequestDetailsActivity.this)
                        .setTitle("Confirm Location")
                        .setMessage("Are you sure you want to save these Location details?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent output = new Intent();
                                output.putExtra("Saved Location", location);
                                setResult(RESULT_OK, output);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

            }
        });







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
}