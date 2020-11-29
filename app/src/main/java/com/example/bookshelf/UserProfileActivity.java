package com.example.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

/**
 * User profile activity for viewing user information.
 */
public class UserProfileActivity extends AppCompatActivity implements EditProfileFragment.OnFragmentInteractionListener {

    //Layout Variables
    private Button signoutBtn;
    private ImageView profilePicImageV;
    private TextView usernameTv;
    private TextView fullnameTv;
    private TextView emailTv;
    private TextView phoneTv;
    private FloatingActionButton editBtn;

    //UserInfo object
    private UserInfo userInfo;

    //Database instance
    private FirebaseFirestore db;

    //Storage and Upload Imaging vrariables
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference;
    private Uri uri;

    /**
     * The User for authentication
     */
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Layout Assignments
        usernameTv = findViewById(R.id.profile_username);
        fullnameTv = findViewById(R.id.profile_fullname);
        emailTv = findViewById(R.id.profile_email);
        phoneTv = findViewById(R.id.profile_phone);
        editBtn = findViewById(R.id.profile_edit_btn);

        //Database instance initialization
        db = FirebaseFirestore.getInstance();
        storageReference = storage.getReference();

        //Fill Data in page
        db.collection("users").document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();

                            //UserInfo Object Creation.
                            userInfo = documentSnapshot.toObject(UserInfo.class);

                            //Setting Textview fields
                            usernameTv.setText(userInfo.getUsername());
                            fullnameTv.setText(userInfo.getFullname());
                            emailTv.setText(userInfo.getEmail());
                            phoneTv.setText(userInfo.getPhone());

                            //Loading user profile pic
//                            storageReference.child(photoURL).getDownloadUrl().addOnSuccessListener(
//                                    new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//                                            Picasso.get().load(uri).into(bookIm);
//                                        }
//                                    }
//                            ).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(getApplicationContext(), "Failed to fetch image.", Toast.LENGTH_SHORT).show();
//                                }
//                            });


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
                new EditProfileFragment(userInfo).show(getSupportFragmentManager(), "EDIT");
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

    /**
     * When the user information has been updated.
     * Update the fields locally and in the database.
     * @param userInfo the user info
     */
    @Override
    public void onOkPressed(UserInfo userInfo) {
        //Update local fields
        usernameTv.setText(userInfo.getUsername());
        fullnameTv.setText(userInfo.getFullname());
        emailTv.setText(userInfo.getEmail());
        phoneTv.setText(userInfo.getPhone());

//        //Update user email for signin purposes as well
//        user.updateEmail(userInfo.getEmail());

        //Update Locally
        this.userInfo = userInfo;

        //Update database
        db.collection("users").document(user.getUid()).update(userInfo.getUserMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Successfully updated user information.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to update user information. Check Console.", Toast.LENGTH_SHORT).show();
            }
        });

    }

}