package com.example.bookshelf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RequestView extends AppCompatActivity {
    TextView title, author, isbn, owner, requester;
    ImageView imageView;
    Button accept, decline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_view);
        Intent intent = getIntent();
        String info = intent.getStringExtra("Book Info");

        accept = findViewById(R.id.request_accept);
        decline = findViewById(R.id.request_decline);




    }
}