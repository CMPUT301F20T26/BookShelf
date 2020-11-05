package com.example.bookshelf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class UserActivity extends AppCompatActivity {
    private ImageView profilePic;
    private TextView fullName;
    private TextView name;
    private TextView email;
    private TextView cellphone;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        String message = intent.getStringExtra(BookActivity.EXTRA_MESSAGE);

        db = FirebaseFirestore.getInstance();

        profilePic = findViewById(R.id.profile_picture);
        fullName = findViewById(R.id.full_name);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        cellphone = findViewById(R.id.cellphone);

        name.setText(message);




    }
}