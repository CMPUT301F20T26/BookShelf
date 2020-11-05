package com.example.bookshelf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Welcome page activity that facilitates user persistence
 */
public class MainActivity extends AppCompatActivity {
    //Current user
    FirebaseUser user;

    //Layout variables
    private Button nextButton;

    /**
     * On create lifecycle method for creating the activity
     * @param savedInstanceState current instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Go to log in page when user clicks button
        nextButton = findViewById(R.id.next_btn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createAccountIntnet = new Intent(getApplicationContext(), CreateAccountActivity.class);
                startActivity(createAccountIntnet);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        //User Persistence
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
            i.putExtra("UserID", user.getUid());
            startActivity(i);
        }
    }


}