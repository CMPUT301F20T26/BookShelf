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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

public class BookActivity extends AppCompatActivity implements MakeRequestFragment.OnFragmentInteractionListener,
        AddBookFragment.DialogListener, BookRequestersViewFragment.DialogListener {
    /**
     * The constant EXTRA_MESSAGE.
     */

    public static final String EXTRA_MESSAGE = "com.example.bookshelf.MESSAGE";
    private ImageView displayPic;
    private TextView title;
    private TextView description;
    private TextView author;
    private TextView ISBN;
    private TextView owner;
    private TextView status;
    private String bookId;
    private FloatingActionButton bookEditBtn;
    private FloatingActionButton bookRequestsBtn;

    //Firebase authentication instance
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    //Database Instance definition
    private FirebaseFirestore db = FirebaseFirestore.getInstance();;
    private CollectionReference bookCollection = db.collection("books");
    private DocumentReference currentUserDocument = db.collection("users").document(currentUser.getUid());

    //Firebase Storage instance
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    final StorageReference storageReference  = storage.getReference();

    private Book currentBook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        //intent should provide bookID, use to access object and set fields
        //right now it only contains a string of the book's title

        Intent intent = getIntent();
        String message = intent.getStringExtra(SearchBooksActivity.EXTRA_MESSAGE);
        bookId = message;

        displayPic = findViewById(R.id.imageView);
        title = findViewById(R.id.title_text);
        description = findViewById(R.id.book_description);
        author = findViewById(R.id.author_text);
        ISBN = findViewById(R.id.isbn_text);
        ISBN.setEnabled(false);
        owner = findViewById(R.id.owner_text);
        status = findViewById(R.id.status_text);
        bookEditBtn = findViewById(R.id.book_edit);
        bookRequestsBtn = findViewById(R.id.book_requesters);

        bookCollection.document(message)
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
                            ).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "No image attached to book.", Toast.LENGTH_SHORT).show();
                                }
                            });

                            BookFactory currentFactory = new BookFactory("books");
                            currentBook = currentFactory.get(document);

                            //Filling book values
                            title.setText(currentBook.getTitle());
                            author.setText(currentBook.getAuthor());
                            ISBN.setText(currentBook.getIsbn().toString());
                            owner.setText(currentBook.getOwnerUsername());
                            status.setText(currentBook.getStatus().toString());
                            description.setText(currentBook.getDescription());

                            //Hiding the book edit button if you are not the owner of the book.
                            currentUserDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    String currentUserUsername = task.getResult().getData().get("username").toString();
                                    if(!currentUserUsername.trim().equals(currentBook.getOwnerUsername().trim())){
                                        bookEditBtn.setVisibility(View.INVISIBLE);
                                        bookRequestsBtn.setVisibility(View.INVISIBLE);
                                    }else{
                                        //Starting the edit book fragment on edit book click
                                        bookEditBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                AddBookFragment add_new = AddBookFragment.newInstance(currentBook);
                                                add_new.show(getSupportFragmentManager(),"EDIT_GEAR");
                                            }
                                        });

                                        bookRequestsBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                new BookRequestersViewFragment(currentBook.getBookID()).show(getSupportFragmentManager(), "REQUESTERS");
                                            }
                                        });
                                    }
                                }
                            });

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
                final String username = owner.getText().toString();

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

        RequestTheirBook.requestNew(currentBook);
    }

    /**
     * Takes the user id and passes it to UserActivity
     *
     * @param id book id to be passed
     */
    public void openUserProfile(String id){
        Intent intent = new Intent(this, UserActivity.class);
        // we want the message to be the book ID corresponding to the selected book
        intent.putExtra(EXTRA_MESSAGE, id);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        this.finish();
    }

    @Override
    public void onOkPressed(final Book book, final String author, final String des, final String isbn, final String title,final String photoURL, final Boolean edit) {
        BookFactory bookFactory = new BookFactory("books");
        bookFactory.OwnerUsername(book.getOwnerUsername());
        bookFactory.Author(author);
        bookFactory.Description(des);
        bookFactory.ISBN(Long.parseLong(isbn));
        bookFactory.Title(title);
        bookFactory.CoverImage(photoURL);
        Book newadd;
        if(edit == true){
            bookFactory.Status(book.getStatus());
            newadd = bookFactory.edit(book.getBookID());
        }
        else {}
        bookFactory.edit(book.getBookID());
        bookFactory.New();
        //Refilling Book values
        this.title.setText(title);
        this.author.setText(author);
        this.ISBN.setText(String.valueOf(isbn));
        this.owner.setText(book.getOwnerUsername());
        this.status.setText(book.getStatus().toString());
        this.description.setText(des);

        //Refetching Book image
        String picUrl = "Book Images/" + isbn + ".png";
        storageReference.child(picUrl).getDownloadUrl().addOnSuccessListener(
                new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(displayPic);
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                displayPic.setImageDrawable(null);
                Toast.makeText(getApplicationContext(), "No image attached to book.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}