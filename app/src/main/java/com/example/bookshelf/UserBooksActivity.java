package com.example.bookshelf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class UserBooksActivity extends AppCompatActivity implements AddBookFragment.DialogListener, BookFactory.bookListener {
    //Global Variable to keep track of books
    int pos;
    ArrayAdapter<Book> bookAdapter;
    ListView bookList;
    ArrayList<Book> bookDataList;
    BookFactory bookFactory;
    private FirebaseFirestore db;


    //Firebase Authentication instance
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_books);  //Opening content view

        //Initialize variables
        Button addBookButton;
        addBookButton = findViewById(R.id.add);
        bookList = findViewById(R.id.Book_list);
        bookDataList = new ArrayList<>();
        bookAdapter = new BookArrayAdapter(this,bookDataList);
        bookFactory = new BookFactory("books");
        bookList.setAdapter(bookAdapter);

        /*FirebaseHelper firebaseHelper = new FirebaseHelper();
        DocumentSnapshot o_books=firebaseHelper.get("users",user.getUid());
        List<String> ownedBooks = (List<String>) o_books.getData().get("ownedBooks");*/
        final DocumentSnapshot[] result = new DocumentSnapshot[1];
        db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            List<String> ownedBooks = (List<String>) documentSnapshot.getData().get("ownedBooks");
                            for (int i = 0; i < ownedBooks.size(); i++) {
                                bookFactory.get(ownedBooks.get(i));

                                //bookFactory.give();
                               // System.out.println((bookFactory.ret(ownedBooks.get(i))));
                                //bookAdapter.add(bookFactory.get(ownedBooks.get(i)));
                                //bookAdapter.notifyDataSetChanged();
                            }
                        }}});

        // Add button to add a book, opens a fragment, and adds a book
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    new AddBookFragment().show(getSupportFragmentManager(), "ADD_BOOK");
                }
        });




        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                pos = position;//to keep track of which item to edit
                System.out.println(pos);

                // when delete button pressed remove the selected item from GearAdapter
                /*Button deleteButton = findViewById(R.id.delete);
                deleteButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (pos < bookList.getCount() && pos >= 0) {
                                String rem_isbn = (bookDataList.get(pos)).getISBN().toString();
                                collectionReference
                                        .document(rem_isbn)
                                        .delete();
                            bookDataList.remove(pos);
                            bookAdapter.notifyDataSetChanged();
                            pos = -1;
                    }
                }});*/

                // when edit button clicked go to AddGearFragment to edit
                /*Button editButton = findViewById(R.id.edit);
                editButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (pos < bookList.getCount() && pos >= 0) {
                            AddBookFragment add_new = AddBookFragment.newInstance(bookAdapter.getItem(pos));
                            add_new.show(getSupportFragmentManager(),"EDIT_GEAR");
                            Book dbadd = bookDataList.get(pos);
                            Map<String, Object> book = new HashMap<>();
                            book.put("title", dbadd.getTitle());
                            book.put("author", dbadd.getAuthor());
                            book.put("isbn", dbadd.getISBN().toString());
                            book.put("description", dbadd.getDescription());
                            collectionReference
                                    .document(dbadd.getISBN().toString())
                                    .update(book);
                            pos = -1;
                        }
                    }
                });*/
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
    public void onOkPressed(String author,String des,String isbn,String title) {
        bookFactory.Author(author);
        bookFactory.Description(des);
        bookFactory.ISBN(Long.parseLong(isbn));
        bookFactory.Title(title);
        bookFactory.Status(Book.BookStatus.Available);
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        DocumentSnapshot owner=firebaseHelper.get("users",user.getUid());
        //Log.d("Error",owner.toString());
//        String user_name = owner.getData().get("username").toString();
//        bookFactory.OwnerUsername(user_name);
        bookAdapter.add(bookFactory.build());
        bookAdapter.notifyDataSetChanged();
        bookFactory.New();
    }

    @Override
    public void getBook(Book book) {
        bookAdapter.add(book);
        bookAdapter.notifyDataSetChanged();
    }
}