package com.example.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserNotificationsActivity extends AppCompatActivity {

    private ListView notificationList;
    private NotificationAdapter notificationAdapter;
    private FirebaseFirestore db;

    //Layout variables
    TextView uidTv;

    //Firebase Authentication instance
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notifications);

        db = FirebaseFirestore.getInstance();

        notificationList = findViewById(R.id.notification_list);

        final ArrayList<ListNotifications> notifications = new ArrayList<ListNotifications>();
        notificationAdapter = new NotificationAdapter(this, notifications);
        notificationList.setAdapter(notificationAdapter);

        db.collection("notifications")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                ListNotifications displayNotification = ListNotifications.get(document);
                                matchNotification(displayNotification);
                            }
                        }
                    }
                });

//        final String userId = user.getUid();
//        uidTv = findViewById(R.id.uid_notifications);
//        uidTv.setText(userId);



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
                switch (menuItem.getItemId()){
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

    public void matchNotification(ListNotifications notification){
        final String userId = user.getUid();

//        if((notification.getOwnerID().equals(userId) && notification.getStatus() == RequestStatus.PENDING) || (notification.getRequesterID().equals(userId) && notification.getStatus() == RequestStatus.ACCEPTED)) {
            notificationAdapter.add(notification);
//        }

    }

}