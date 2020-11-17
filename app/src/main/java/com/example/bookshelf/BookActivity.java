package com.example.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


/**
 * This activity is for viewing the details of a book that has been searched for, and allowing the user to make a borrow request on the book.
 */

public class BookActivity extends AppCompatActivity implements MakeRequestFragment.OnFragmentInteractionListener {
    public static final String EXTRA_MESSAGE = "com.example.bookshelf.MESSAGE";
    private ImageView displayPic;
    private TextView title;
    private TextView description;
    private TextView author;
    private TextView ISBN;
    private TextView owner;
    private TextView status;

    //Database Instance definition
    private FirebaseFirestore db;

    //Firebase Storage instance
    private FirebaseStorage storage = FirebaseStorage.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        db = FirebaseFirestore.getInstance();
        final StorageReference storageReference  = storage.getReference();

        //intent should provide bookID, use to access object and set fields
        //right now it only contains a string of the book's title

        Intent intent = getIntent();
        String message = intent.getStringExtra(SearchBooksActivity.EXTRA_MESSAGE);

        displayPic = findViewById(R.id.imageView);
        title = findViewById(R.id.title_text);
        description = findViewById(R.id.book_description);
        author = findViewById(R.id.author_text);
        ISBN = findViewById(R.id.isbn_text);
        owner = findViewById(R.id.owner_text);
        status = findViewById(R.id.status_text);

        db.collection("books")
                .document(message)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            //Book Document
                            DocumentSnapshot document = task.getResult();

                            //Getting book cover image
                            String picUrl = "Book Images/" + document.getData().get("coverImage");
                            storageReference.child(picUrl).getDownloadUrl().addOnSuccessListener(
                                    new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Picasso.get().load(uri).into(displayPic);
                                        }
                                    }
                            );

                            //Filling book values
                            title.setText(document.getData().get("title").toString());
                            author.setText(document.getData().get("author").toString());
                            ISBN.setText(document.getData().get("isbn").toString());
                            owner.setText(document.getData().get("ownerUsername").toString());
                            status.setText(document.getData().get("status").toString());
                            description.setText(document.getData().get("description").toString());

                        }
                    }
                });

        final HashMap<String, String> userId = new HashMap<String, String>();

        final Button request = findViewById(R.id.request_button);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MakeRequestFragment().show(getSupportFragmentManager(), "CONFIRM");
            }
        });


        owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                userId.clear();
                final String username = owner.getText().toString();

                // function that generates intent and starts activity
                db.collection("users").whereEqualTo("username", username).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        userId.put(document.getData().get("username").toString(), document.getId());
                                    }

                                    if(userId.containsKey(username)){
                                        openUserProfile(userId.get(username));
                                    }
                                }
                            }
                        });
            }
        });

    }

    /**
     * sets book as requested in users profile, sends request notification to owner of the book
     */
    public void onOkPressed(){
        //do nothing, for now
    }

    /**
     * Takes the user id and passes it to UserActivity
     *
     * @param id String to be passed
     */
    public void openUserProfile(String id){
        Intent intent = new Intent(this, UserActivity.class);
        // we want the message to be the book ID corresponding to the selected book
        intent.putExtra(EXTRA_MESSAGE, id);
        startActivity(intent);
    }
}