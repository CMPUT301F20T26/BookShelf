package com.example.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserBooks extends AppCompatActivity implements AddBookFragment.DialogListener {
    TextView uidTv;
    ListView bookList;
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> bookDataList;
    int pos;
    CustomList customList;
    private List<Book> Books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_books);

        final String userId = getIntent().getStringExtra("UserID");
        //uidTv = findViewById(R.id.uid_userBooks);
        //uidTv.setText(userId);
        Button addBookButton;
        addBookButton = findViewById(R.id.add);
        bookList = findViewById(R.id.Book_list);
        bookDataList = new ArrayList<>();
        bookAdapter = new CustomList(this,bookDataList);
        final FirebaseFirestore db;

        bookList.setAdapter(bookAdapter);
        String titl = "Twiligt";
        String auth = "Twiligt";
        String desc = "Twiligt";
        Long isb = (long) 124684641;
        Book bookadd = new Book(titl, auth, desc, isb);
        bookAdapter.add(bookadd);
        bookAdapter.add(bookadd);
        bookAdapter.add(bookadd);
        bookAdapter.add(bookadd);
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("books");
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
                Button deleteButton = findViewById(R.id.delete);
                deleteButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (pos < bookList.getCount() && pos >= 0) {
                            if (pos < bookList.getCount() && pos >= 0) {
                                String rem_isbn = (bookDataList.get(pos)).getISBN().toString();
                                collectionReference
                                        .document(rem_isbn)
                                        .delete();
                            bookDataList.remove(pos);
                            bookAdapter.notifyDataSetChanged();
                            pos = -1;
                        }
                    }
                }});

                // when edit button clicked go to AddGearFragment to edit
                Button editButton = findViewById(R.id.edit);
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
                });
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
                        Intent profileIntent = new Intent(getApplicationContext(), UserProfile.class);
                        profileIntent.putExtra("UserID", userId);
                        startActivity(profileIntent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.notifications_page:
                        Intent notificationIntent = new Intent(getApplicationContext(), UserNotifications.class);
                        notificationIntent.putExtra("UserID", userId);
                        startActivity(notificationIntent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.books_page:
                        return true;
                    case R.id.search_page:
                        Intent searchIntent = new Intent(getApplicationContext(), SearchBooks.class);
                        searchIntent.putExtra("UserID", userId);
                        startActivity(searchIntent);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
        //__________________________________________________________________________________________

    }
    @Override
    public void add_Book(Book book) {
        bookAdapter.add(book);
    }

    @Override
    public void edit_Book(Book book, String title, String author, Long isbn, String des) {
        book.setTitle(title);
        book.setAuthor(author);
        book.setISBN(isbn);
        book.setDescription(des);
        bookAdapter.notifyDataSetChanged();
    }
}