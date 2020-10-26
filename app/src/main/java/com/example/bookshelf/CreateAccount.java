package com.example.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class CreateAccount extends AppCompatActivity {

    private EditText userFullName;
    private EditText userName;
    private EditText user_email;
    private EditText userPhone;
    private EditText userPassword;
    private Button createAccountBtn;
    private TextView signInText;

    //Firebase Authentication
    private FirebaseAuth userAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        userFullName = findViewById(R.id.full_name);
        userName = findViewById(R.id.user_name);
        user_email = findViewById(R.id.user_email);
        userPhone = findViewById(R.id.phone_number);
        userPassword = findViewById(R.id.user_pwd);
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
                userAuth.createUserWithEmailAndPassword(user_email.getText().toString(), userPassword.getText().toString())
                        .addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "User Sucessfully created. Check Console", Toast.LENGTH_SHORT ).show();
                                    //Start Profile Page activity
                                }else{
                                    Toast.makeText(getApplicationContext(), "User FAILED TO create. Check Console", Toast.LENGTH_SHORT ).show();
                                }
                            }
                        });
            }
        });

    }
}