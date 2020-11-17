package com.example.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;


/**
 * This activity displays a user's profile and contact info who is the owner of some selected book.
 */

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
        String userid = intent.getStringExtra(BookActivity.EXTRA_MESSAGE);

        db = FirebaseFirestore.getInstance();

        profilePic = findViewById(R.id.profile_picture);
        fullName = findViewById(R.id.full_name);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        cellphone = findViewById(R.id.cellphone);

        db.collection("users").document(userid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();

                            name.setText(documentSnapshot.getData().get("username").toString());
                            fullName.setText(documentSnapshot.getData().get("fullname").toString());
                            email.setText(documentSnapshot.getData().get("email").toString());
                            cellphone.setText(documentSnapshot.getData().get("phone").toString());
                        }
                    }
                });

    }
}