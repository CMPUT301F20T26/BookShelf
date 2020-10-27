package com.example.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {

    private EditText userFullNameEt;
    private EditText userNameEt;
    private EditText user_emailEt;
    private EditText userPhoneEt;
    private EditText userPasswordEt;
    private Button createAccountBtn;
    private TextView signInText;

    //Firebase Authentication
    private FirebaseAuth userAuth;

    //Firebase Cloud Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        userFullNameEt = findViewById(R.id.create_account_full_name);
        userNameEt = findViewById(R.id.create_account_user_name);
        user_emailEt = findViewById(R.id.create_account_user_email);
        userPhoneEt = findViewById(R.id.create_account_phone_number);
        userPasswordEt = findViewById(R.id.create_account_user_pwd);
        signInText = findViewById(R.id.sign_in_text);
        createAccountBtn = findViewById(R.id.create_account_btn);

        //Initialize firebase authetication instance
        userAuth = FirebaseAuth.getInstance();

        //Creating a password based user account
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Add password verification and password masking
                //TODO: Add phoneNumber verification
                //TODO: Add userName Verification
                //TODO: Add Is empty checks
                userAuth.createUserWithEmailAndPassword(user_emailEt.getText().toString(), userPasswordEt.getText().toString())
                        .addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser authenticatedUser = userAuth.getCurrentUser();

                                    //Add user data to cloud database
                                    Map<String, String> user = new HashMap<>();
                                    user.put("fullname", userFullNameEt.getText().toString());
                                    user.put("username", userNameEt.getText().toString());
                                    user.put("email", user_emailEt.getText().toString());
                                    user.put("phone", userPhoneEt.getText().toString());

                                    //Add user to database using userID in authentication
                                    db.collection("users").document(authenticatedUser.getUid()).set(user);

                                    //Start Profile Page activity
                                    //Pass authenticated user id into profile page
                                    Intent profilePageIntent = new Intent(getApplicationContext(), UserProfile.class);
                                    profilePageIntent.putExtra("UserID", authenticatedUser.getUid());

                                    startActivity(profilePageIntent);
                                }else{
                                    Toast.makeText(getApplicationContext(), "User FAILED TO create. Check Console", Toast.LENGTH_SHORT ).show();
                                }
                            }
                        });
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