package com.example.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
public class UserBooksActivity extends AppCompatActivity implements AddBookFragment.DialogListener, DeleteConfirmFragment.DialogListener{
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

    /**
     * Instantiates a new User books activity.
     */
    public UserBooksActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_books);

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
        bookList.setAdapter(bookAdapter);

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
                        AddBookFragment add_new = AddBookFragment.newInstance(clickedBook, i);
                        add_new.show(getSupportFragmentManager(),"EDIT_GEAR");
                    }
                });
            }
        });

        // TODO: clean up test code, integrate with benefactory
        //String titl = "Twiligt";
        //String auth = "Twiligt";
        //String desc = "Twiligt";
        //Long isb = (long) 124684641;
        //Book bookadd = new Book(titl, auth, desc, isb,"me");
        //bookAdapter.add(bookadd);
        //bookAdapter.add(bookadd);
        //bookAdapter.add(bookadd);
        //bookAdapter.add(bookadd);

        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    new AddBookFragment().show(getSupportFragmentManager(), "ADD_BOOK");
                }
        });

        // I am grabbing the username and owned books list, from the user database and saving it in the user_name,
        // I checked whether its grabbing it by sending it to the log
        userDocument.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot userDocumentSnapshot = task.getResult();
                            user_name = userDocumentSnapshot.getData().get("username").toString();
                            ArrayList<String> userOwnedBooks = (ArrayList<String>) userDocumentSnapshot.getData().get("ownedBooks");
                            for (final String bookId : userOwnedBooks) {
                                bookCollection.document(bookId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot bookDocumentSnapshot = task.getResult();

                                            Book tempBook = new Book();

                                            tempBook.setTitle(bookDocumentSnapshot.getData().get("title").toString());

                                            tempBook.setCoverImage(bookDocumentSnapshot.getData().get("coverImage").toString());

                                            tempBook.setDescription(bookDocumentSnapshot.getData().get("description").toString());

                                            tempBook.setOwnerUsername(bookDocumentSnapshot.getData().get("ownerUsername").toString());

                                            tempBook.setStatus((Book.BookStatus.valueOf(bookDocumentSnapshot.getData().get("status").toString())));

                                            tempBook.setAuthor(bookDocumentSnapshot.getData().get("author").toString());

                                            tempBook.setBookID(bookId);

                                            tempBook.setBookID(bookDocumentSnapshot.getId());
                                            String tempIsbnString = bookDocumentSnapshot.getData().get("isbn").toString().replace("-", "");
                                            Long tempIsbn = Long.parseLong(tempIsbnString);
                                            tempBook.setIsbn(tempIsbn);

                                            bookDataList.add(tempBook);
                                            bookAdapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
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
                    case R.id.maps_page:
                        Intent mapsIntent = new Intent(getApplicationContext(), RequestDetailsActivity.class);
                        startActivity(mapsIntent);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        //__________________________________________________________________________________________

    }

    @Override
    public void add_Book(String title, String author, Long isbn, String photoURL, String ownerUsername, String description) {
        final Book editedBook = new Book();
        editedBook.setTitle(title);
        editedBook.setAuthor(author);
        editedBook.setIsbn(isbn);
        editedBook.setDescription(description);
        editedBook.setCoverImage(photoURL);
        editedBook.setOwnerUsername(ownerUsername);
        editedBook.setStatus(Book.BookStatus.Available);

        bookCollection.add(editedBook.getBookFirebaseMap()).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                editedBook.setBookID(task.getResult().getId());
                userDocument.update("ownedBooks", FieldValue.arrayUnion(task.getResult().getId())).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Successfully Added new book", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        bookDataList.add(editedBook);
        bookAdapter.notifyDataSetChanged();
    }

    @Override
    public void edit_Book(String title, final String author, final Long isbn, final String photoURL, final String ownerUsername, final String description, Book.BookStatus status, Integer position, final String bookId) {
        final Book editedBook = new Book();
        editedBook.setTitle(title);
        editedBook.setAuthor(author);
        editedBook.setIsbn(isbn);
        editedBook.setDescription(description);
        editedBook.setCoverImage(photoURL);
        editedBook.setOwnerUsername(ownerUsername);
        editedBook.setStatus(status);


        //Book exists already. Update books collection only.
        bookCollection.document(bookId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();

                editedBook.setBookID(bookId);
                bookCollection.document(documentSnapshot.getId()).update(
                        "author",author,
                        "description", description,
                        "coverImage",photoURL,
                        "description",description,
                        "isbn",isbn
                ).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Successfully updated book", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        bookDataList.set(position, editedBook);
        bookAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteLongPress(Integer position){
        final Book deletedBook = bookDataList.get(position);

        //Delete from delete from books collection
        bookCollection.document(deletedBook.getBookID()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                userDocument.update("ownedBooks", FieldValue.arrayRemove(deletedBook.getBookID())).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Successfully deleted " + deletedBook.getTitle(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //Delete cover image from firebase storage.
        StorageReference bookCoverRef = mStorageRef.child("Book Images/"+ deletedBook.getPhotoURL());
        bookCoverRef.delete();

        //Delete from book data list and update adapter
        bookDataList.remove(deletedBook);
        bookAdapter.notifyDataSetChanged();

    }


}