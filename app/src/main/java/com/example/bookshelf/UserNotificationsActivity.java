package com.example.bookshelf;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserNotificationsActivity extends AppCompatActivity {

    private ListView notificationListView;
    private NotificationAdapter notificationAdapter;
    private FirebaseFirestore db;

    //Layout variables
    TextView uidTv;

    //Firebase Authentication instance
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final ArrayList<ListNotifications> notifications = new ArrayList<ListNotifications>();
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notifications);

        db = FirebaseFirestore.getInstance();

        notificationListView = findViewById(R.id.notification_list);

        final ArrayList<ListNotifications> notificationsList = new ArrayList<ListNotifications>();
        notificationAdapter = new NotificationAdapter(this, notificationsList);
        notificationListView.setAdapter(notificationAdapter);

        final FirebaseHelper helper = new FirebaseHelper();

        final FirebaseHelper.IHelper notifListener = new FirebaseHelper.IHelper() {
            @Override
            public void onSuccess(Object o) {
                final DocumentSnapshot res = (DocumentSnapshot) o;
                ListNotifications n = ListNotifications.get(res);
                if (n != null) {
                    notificationsList.add(n);

                }
                notificationAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(Object o) {

            }
        };

        FirebaseHelper.IHelper arrayListener = new FirebaseHelper.IHelper() {
            @Override
            public void onSuccess(Object o) {
                final List<String> myNotif = (List<String>) o;
                for (String n : myNotif) {
                    helper.get("notifications", n, notifListener);

                }
            }

            @Override
            public void onFailure(Object o) {

            }
        };

        helper.getUserArray("notifications", arrayListener);


        //BOTTOM NAVIGATION_________________________________________________________________________
        //Initialize nav bar and assign it
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);

        // Set home selected
        bottomNavigationView.setSelectedItemId(R.id.notifications_page);

        //Item Selected Listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //Pass User's UID into activities on menu click
                switch (menuItem.getItemId()) {
                    case R.id.profile_page:
                        Intent profileIntent = new Intent(getApplicationContext(), UserProfileActivity.class);
                        startActivity(profileIntent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.notifications_page:
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
                        Intent mapsIntent = new Intent(getApplicationContext(), RequestDetailsActivity.class);
                        startActivity(mapsIntent);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        //__________________________________________________________________________________________
    }


}