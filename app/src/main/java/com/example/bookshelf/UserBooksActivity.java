package com.example.bookshelf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * The type User books activity.
 */
public class UserBooksActivity extends AppCompatActivity implements DeleteConfirmFragment.DialogListener, AddBookFragment.DialogListener, BookFactory.bookListener{
    //Layout variables
    private ListView bookList;
    private FloatingActionButton addBookButton;

    //Adapter and List view variables
    private ArrayAdapter<Book> bookAdapter;
    private ArrayList<Book> bookDataList;
//    private int pos;

    //Firebase Authentication instance
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //Current user's username
    private String user_name;
    private String userId;

    //Database Initialization.
    private FirebaseFirestore db =  FirebaseFirestore.getInstance();
    //Books Collection reference
    private CollectionReference bookCollection = db.collection("books");
    //Current user's collection reference
    private DocumentReference userDocument;

    //Firebase Storage
    private StorageReference mStorageRef;

    //Firebase helper instantiation.
    private FirebaseHelper firebaseHelper;

    //Extra message handler
    public static final String EXTRA_MESSAGE = "com.example.bookshelf.MESSAGE";

    //BookFactory call
    private BookFactory bookFactory;

    /**
     * Instantiates a new User books activity.
     */
    public UserBooksActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_books);  //Opening content view

       //Initialize variables
        final FloatingActionButton addBookButton;

        //Current user id
        userId = user.getUid();
        userDocument = db.collection("users").document(userId);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //Layout Assignments
        addBookButton = findViewById(R.id.add);
        bookList = findViewById(R.id.Book_list);

        //Adapter assignments
        bookDataList = new ArrayList<>();
        bookAdapter = new BookArrayAdapter(this,bookDataList);

        bookFactory = new BookFactory("books");
        bookList.setAdapter(bookAdapter);
        db = FirebaseFirestore.getInstance();

        //Long Press to delete
        bookList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Show confirmation fragment
                new DeleteConfirmFragment(i).show(getSupportFragmentManager(), "DELETE");
                return false;
            }
        });

        //Click to edit
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Book clickedBook = bookDataList.get(i);

                        openBookDescription(clickedBook.getBookID());
                    }
                });
            }
        });

        //Clicking the add button
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    new AddBookFragment().show(getSupportFragmentManager(), "ADD_BOOK");
                }
        });

        // I am grabbing the username and owned books list, from the user database and saving it in the user_name,
        // I checked whether its grabbing it by sending it to the log
        getUserOwnedBooks();

        //BOTTOM NAVIGATION_________________________________________________________________________
        //Initialize nav bar and assign it
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);

        // Set home selected
        bottomNavigationView.setSelectedItemId(R.id.books_page);

        //Item Selected Listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //Pass User's UID into activities on menu click
                switch (menuItem.getItemId()) {
                    case R.id.profile_page:
                        Intent profileIntent = new Intent(getApplicationContext(), UserProfileActivity.class);
                        startActivity(profileIntent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.notifications_page:
                        Intent notificationIntent = new Intent(getApplicationContext(), UserNotificationsActivity.class);
                        startActivity(notificationIntent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.books_page:
                        return true;
                    case R.id.search_page:
                        Intent searchIntent = new Intent(getApplicationContext(), SearchBooksActivity.class);
                        startActivity(searchIntent);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        //__________________________________________________________________________________________
    }

    private void getUserOwnedBooks() {

        bookDataList.clear();
        db.collection("users").document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            final List<String> ownedBooks = (List<String>) documentSnapshot.getData().get("ownedBooks");
                            for(int i = 0; i<ownedBooks.size(); i++){
                                System.out.println(ownedBooks.get(i));
                                db.collection("books").document(ownedBooks.get(i)).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot documentSnapshot = task.getResult();
                                                    getBook(bookFactory.get(documentSnapshot));

                                                }
                                            }});}}}});
    }

    @Override
    public void onOkPressed(final Book book, final String author, final String des, final String isbn, final String title,final String photoURL, final Boolean edit) {
        db.collection("users").document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            bookFactory.OwnerUsername(documentSnapshot.getData().get("username").toString());
                            bookFactory.Author(author);
                            bookFactory.Description(des);
                            bookFactory.ISBN(Long.parseLong(isbn));
                            bookFactory.Title(title);
                            bookFactory.CoverImage(photoURL);
                            Book newadd;
                            if(edit == true){}
                            else {
                                bookFactory.Status(Book.BookStatus.Available);
                                newadd = bookFactory.build();
                                bookDataList.add(newadd);
                                userDocument.update("ownedBooks", FieldValue.arrayUnion(newadd.getBookID()));
                            }
                            bookAdapter.notifyDataSetChanged();
                            bookFactory.New();
                        }
                    }
                });
    }

    @Override
    public void getBook(Book book) {
        bookAdapter.add(book);
    }
    public void deleteLongPress(Integer position){
        final Book deletedBook = bookDataList.get(position);

        //Delete from delete from books collection
        bookFactory.delete(deletedBook.getBookID());
        userDocument.update("ownedBooks", FieldValue.arrayRemove(deletedBook.getBookID()));

        //Delete cover image from firebase storage.
        StorageReference bookCoverRef = mStorageRef.child("Book Images/"+ deletedBook.getPhotoURL());
        bookCoverRef.delete();

        //Delete from book data list and update adapter
        bookDataList.remove(deletedBook);
        bookAdapter.notifyDataSetChanged();

    }

    public void openBookDescription(String id) {
        Intent intent = new Intent(this, BookActivity.class);
        // we want the message to be the book ID corresponding to the selected book
        intent.putExtra(EXTRA_MESSAGE, id);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && resultCode==RESULT_OK) {
            getUserOwnedBooks();
        }
    }
}