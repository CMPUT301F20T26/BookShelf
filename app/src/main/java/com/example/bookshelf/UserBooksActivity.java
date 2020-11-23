package com.example.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserBooksActivity extends AppCompatActivity implements AddBookFragment.DialogListener, BookFactory.bookListener {
    //Global Variable to keep track of books
    int pos;
    public static final String EXTRA_MESSAGE = "com.example.bookshelf.MESSAGE";
    ArrayAdapter<Book> bookAdapter;
    ListView bookList;
    ArrayList<Book> bookDataList;
    BookFactory bookFactory;
    private FirebaseFirestore db;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_books);  //Opening content view

        //Initialize variables
        final Button addBookButton;
        addBookButton = findViewById(R.id.add);
        bookList = findViewById(R.id.Book_list);
        bookDataList = new ArrayList<>();
        bookAdapter = new BookArrayAdapter(this,bookDataList);
        bookFactory = new BookFactory("books");
        bookList.setAdapter(bookAdapter);
        db = FirebaseFirestore.getInstance();

        // Extract books from the owner username and send to bookAdapter
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

        // Add button to add a book, opens a fragment, and adds a book
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    new AddBookFragment().show(getSupportFragmentManager(), "ADD_BOOK");
                }
        });



        bookList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                openBookDescription(bookDataList.get(position).getBookID());
                return false;
            }
        });
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                pos = position;//to keep track of which item to edit
                System.out.println(pos);

                // when delete button pressed remove the selected item from BookAdapter
                Button deleteButton = findViewById(R.id.delete);
                deleteButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (pos < bookList.getCount() && pos >= 0) {
                            String remove_id = (bookDataList.get(pos)).getBookID();
                            //TODO: remove the book from user owned books
                            bookFactory.delete(remove_id);
                            bookDataList.remove(pos);
                            bookAdapter.notifyDataSetChanged();
                            pos = -1;
                    }
                }});

                // when edit button clicked go to AddBookFragment to edit
                Button editButton = findViewById(R.id.edit);
                editButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (pos < bookList.getCount() && pos >= 0) {
                            AddBookFragment add_new = AddBookFragment.newInstance(bookAdapter.getItem(pos));
                            add_new.show(getSupportFragmentManager(),"EDIT_GEAR");
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
    public void onOkPressed(final Book book, final String author, final String des, final String isbn, final String title, final Boolean edit) {
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
                            Book newadd;
                            if(edit == true){
                                bookFactory.Status(book.getStatus());
                                newadd = bookFactory.edit(book.getBookID());
                                pos = bookAdapter.getPosition(book);
                                bookDataList.set(pos,newadd);
                            }
                            else {
                                bookFactory.Status(Book.BookStatus.Available);
                                newadd = bookFactory.build();
                                bookDataList.add(newadd);
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
        bookAdapter.notifyDataSetChanged();
        System.out.println(book.getTitle());
    }
    public void openBookDescription(String id) {
        Intent intent = new Intent(this, BookActivity.class);
        // we want the message to be the book ID corresponding to the selected book
        intent.putExtra(EXTRA_MESSAGE, id);
        startActivity(intent);
    }
}