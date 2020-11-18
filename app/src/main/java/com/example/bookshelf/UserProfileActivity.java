package com.example.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * User profile activity
 */
public class UserProfileActivity extends AppCompatActivity implements EditProfileFragment.OnFragmentInteractionListener {

    //Layout Variables
    Button signoutBtn;
    ImageView profilePicImageV;
    TextView usernameTv;
    TextView fullnameTv;
    TextView emailTv;
    TextView phoneTv;
    FloatingActionButton editBtn;

    //Database instance
    FirebaseFirestore db;

    //Firebase Authentication instance
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        final String userId = user.getUid();

        //Layout Assignments
        usernameTv = findViewById(R.id.profile_username);
        fullnameTv = findViewById(R.id.profile_fullname);
        emailTv = findViewById(R.id.profile_email);
        phoneTv = findViewById(R.id.profile_phone);
        editBtn = findViewById(R.id.profile_edit_btn);

        //Database instance initialization
        db = FirebaseFirestore.getInstance();

        //Fill Data
        db.collection("users").document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();

                            usernameTv.setText(documentSnapshot.getData().get("username").toString());
                            fullnameTv.setText(documentSnapshot.getData().get("fullname").toString());
                            emailTv.setText(documentSnapshot.getData().get("email").toString());
                            phoneTv.setText(documentSnapshot.getData().get("phone").toString());

                            //User Object Creation.
                            UserInfo userInfo = documentSnapshot.toObject(UserInfo.class);
                        }
                    }
                });

        //Sign out Button listener
        signoutBtn = findViewById(R.id.signoutbtn);
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth authenticatedUser = FirebaseAuth.getInstance();
                authenticatedUser.signOut();

                Intent signInIntent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(signInIntent);
            }
        });

        //Edit Profile Button Listener set up
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = fullnameTv.getText().toString();
                String email = emailTv.getText().toString();
                String phone = phoneTv.getText().toString();
                new EditProfileFragment().show(getSupportFragmentManager(), "EDIT");
            }
        });

        //BOTTOM NAVIGATION_________________________________________________________________________
        //Initialize nav bar and assign it
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);

        // Set home selected
        bottomNavigationView.setSelectedItemId(R.id.profile_page);

        //Item Selected Listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //Pass User's UID into activities on menu click
                switch (menuItem.getItemId()){
                    case R.id.profile_page:
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

    @Override
    public void onOkPressed(String fullname, String email, String phone) {

    }
}