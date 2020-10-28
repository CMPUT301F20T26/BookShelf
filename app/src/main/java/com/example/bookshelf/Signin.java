package com.example.bookshelf;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signin extends AppCompatActivity {

    private EditText userEmailEt;
    private TextInputLayout userPassLayout;
    private Button logInBtn;
    private TextView createAccountText;

    private FirebaseAuth userAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = userAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        userEmailEt = findViewById(R.id.signin_user_email);
        userPassLayout = findViewById(R.id.signin_password);
        logInBtn = findViewById(R.id.signin_button);
        createAccountText = findViewById(R.id.create_account_text);

        //Verifying user with firebase auth.
        userAuth = FirebaseAuth.getInstance();
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = userEmailEt.getText().toString();
                String userPassword = userPassLayout.getEditText().getText().toString();

                userAuth.signInWithEmailAndPassword(userEmail,userPassword)
                        .addOnCompleteListener(Signin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser authenticatedUser = userAuth.getCurrentUser();

                                    //Pass authenticated user id into profile page
                                    Intent profilePageIntent = new Intent(getApplicationContext(), UserProfile.class);
                                    profilePageIntent.putExtra("UserID", authenticatedUser.getUid());

                                    Toast.makeText(getApplicationContext(), "Welcome Back", Toast.LENGTH_SHORT).show();
                                    startActivity(profilePageIntent);
                                }else{
                                    Toast.makeText(getApplicationContext(), "Sign In failed. Please verify email and password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        //Starting Create Account Activity
        createAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createAccontIntent = new Intent(getApplicationContext(), CreateAccount.class);
                startActivity(createAccontIntent);
            }
        });




    }
}