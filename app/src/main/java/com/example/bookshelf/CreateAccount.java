package com.example.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Create Account activity for users to create credentials to sign into the app.
 */
public class CreateAccount extends AppCompatActivity {

    //Global layout variables
    private EditText userFullNameEt;
    private EditText usernameEt;
    private EditText userEmailEt;
    private EditText userPhoneEt;
    private EditText userPasswordEt;
    private Button createAccountBtn;
    private TextView signInText;

    //Firebase Authentication
    private FirebaseAuth userAuth;

    //Firebase Cloud Firestore
    FirebaseFirestore db;

    /**
     * User hashmap for creating necessary fields in the db
     * @param fullname Full name of the new user
     * @param username chosen username of the new user
     * @param phone chosen phone number of the user
     * @param email chosen email of the user
     * @return a hashmap of necessary fields required to properly create a user
     */
    private HashMap<String, Object> userData(String fullname, String username, String phone , String email){

        HashMap<String, Object> user = new HashMap<String, Object>();
        user.put("email", email);
        user.put("fullname", fullname);
        user.put("notifications", new ArrayList<String>());
        user.put("ownedBooks", new ArrayList<String>());
        user.put("phone", phone);
        user.put("picture", "");
        user.put("username", username);

        return user;
    }


    /**
     * On create lifecycle method for creating the activity
     * @param savedInstanceState current instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //Layout assignments
        userFullNameEt = findViewById(R.id.create_account_full_name);
        usernameEt = findViewById(R.id.create_account_user_name);
        userEmailEt = findViewById(R.id.create_account_user_email);
        userPhoneEt = findViewById(R.id.create_account_phone_number);
        userPasswordEt = findViewById(R.id.create_account_user_pwd);
        signInText = findViewById(R.id.sign_in_text);
        createAccountBtn = findViewById(R.id.create_account_btn);

        //Initialize firebase authentication instance
        userAuth = FirebaseAuth.getInstance();

        //Initialize firebase database
        db = FirebaseFirestore.getInstance();

        //Creating a password based user account
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Special characters are allowed in all fields right now. I'm working on that
                //TODO: Add special characters check to email, password, fullname, username and phonenumber
                final String email = userEmailEt.getText().toString();
                final String password = userPasswordEt.getText().toString();
                final String fullname = userFullNameEt.getText().toString();
                final String username = usernameEt.getText().toString();
                final String phone = userPhoneEt.getText().toString();

                //USER ENTRY CHECKS before account creation
                String numbers = ".*[0-9].*";
                String lettersLowercase = ".*[a-z].*";
                String lettersUppercase = ".*[A-Z].*";

                //Email Check
                if(email.contains("@") && !email.isEmpty()){

                    // Password must have numbers and letters and may have special characters
                    if(!password.isEmpty() && password.matches(numbers) && (password.matches(lettersLowercase) || password.matches(lettersUppercase)) ){

                        //fullName cannot have numbers
                        if(!fullname.matches(numbers) && (fullname.matches(lettersLowercase) || fullname.matches(lettersUppercase))){

                            // username must be letters and numbers only. (No special characters)
                            if(!username.isEmpty()){

                                //Get document with current username (assuming it exists already)
                                db.collection("users").whereEqualTo("username", username).get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()){
                                                    if(task.getResult().isEmpty()) {
                                                        //No user currently has this username
                                                        //Create user
                                                        userAuth.createUserWithEmailAndPassword(email, password)
                                                                .addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                                        if (task.isSuccessful()) {
                                                                            FirebaseUser authenticatedUser = userAuth.getCurrentUser();

                                                                            //Add user to database using userID in authentication
                                                                            db.collection("users").document(authenticatedUser.getUid()).set(userData(fullname, username, phone, email));

                                                                            //Start Profile Page activity
                                                                            //Pass authenticated user id into profile page
                                                                            Intent profilePageIntent = new Intent(getApplicationContext(), UserProfile.class);
                                                                            profilePageIntent.putExtra("UserID", authenticatedUser.getUid());

                                                                            startActivity(profilePageIntent);
                                                                        } else {
                                                                            Toast.makeText(getApplicationContext(), "User FAILED TO create. Check Console", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                    }else{
                                                        Toast.makeText(getApplicationContext(), "User currently in use.", Toast.LENGTH_SHORT).show();
                                                        userEmailEt.requestFocus();
                                                    }
                                                }else{
                                                    Toast.makeText(getApplicationContext(), "Failed to get userdata", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }else{
                                Toast.makeText(getApplicationContext(), "Please enter a username containing letters and numbers only", Toast.LENGTH_SHORT).show();
                                userEmailEt.requestFocus();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Please enter a fullname containing letters only", Toast.LENGTH_SHORT).show();
                            userFullNameEt.requestFocus();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Please enter a password containing numbers and letters.(Special characters permitted)", Toast.LENGTH_SHORT).show();
                        userPasswordEt.requestFocus();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                    userEmailEt.requestFocus();
                }
            }
        });

        //Switching to signin page on click
        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = new Intent(getApplicationContext(), Signin.class);
                startActivity(signInIntent);
            }
        });

    }
}