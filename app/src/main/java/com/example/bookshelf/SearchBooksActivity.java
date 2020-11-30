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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This activity is for search for books that are not currently borrowed or accepted, by their titles or by the user names of those who own them.
 */

public class SearchBooksActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.bookshelf.MESSAGE";
    private ListView searchResults;
    private EditText searchBar;
    private Button searchButton;
    private BookArrayAdapter bookAdapter;
    private FirebaseFirestore db;

    //Firebase Authentication instance
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_books);

        db = FirebaseFirestore.getInstance();

        searchBar = findViewById(R.id.search_bar);
        searchButton = findViewById(R.id.search_button);
        searchResults = findViewById(R.id.search_results);

        final ArrayList<Book> bookList = new ArrayList<Book>();
        bookAdapter = new BookArrayAdapter(this, bookList);
        searchResults.setAdapter(bookAdapter);

        // TODO: not including accept/borrow, my books

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bookList.clear();
                bookAdapter.clear();
                db.collection("books")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        BookFactory searchFactory = new BookFactory("books");
                                        Book findBook = searchFactory.get(document);
                                        matchBook(findBook, bookList);
                                    }
                                }
                            }
                        });
            }
        });

        searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openBookDescription(bookList.get(position).getBookID());
            }
        });


        //BOTTOM NAVIGATION_________________________________________________________________________
        //Initialize nav bar and assign it
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);

        // Set home selected
        bottomNavigationView.setSelectedItemId(R.id.search_page);

        //Item Selected Listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //Pass User's UID into activities on menu click
                switch (menuItem.getItemId()){
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
                        Intent booksIntent = new Intent(getApplicationContext(), UserBooksActivity.class);
                        startActivity(booksIntent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.search_page:
                        return true;
                }
                return false;
            }
        });
        //__________________________________________________________________________________________
    }

    /**
     * Takes a string and matches it against a user string. Matches are added to the
     * custom list and displayed on screen as "results".
     *
     * @param book Book object containing fields
     * @param bookList list of books that matches should be added to
     */
    public void matchBook(Book book, ArrayList<Book> bookList) {
        String check = searchBar.getText().toString();
        Pattern pattern = Pattern.compile(check, Pattern.CASE_INSENSITIVE);
        Matcher titleMatcher = pattern.matcher(book.getTitle());
        Matcher userMatcher = pattern.matcher(book.getOwnerUsername());

        if (!check.isEmpty()) {
            if (titleMatcher.find() || userMatcher.find()) {
                bookAdapter.add(book);
            }
        }
    }

    /**
     * Starts a new activity showing the details of the book that was clicked on.
     *
     * @param id book id to be passed to BookActivity
     */
    public void openBookDescription(String id) {
        Intent intent = new Intent(this, BookActivity.class);
        // we want the message to be the book ID corresponding to the selected book
        intent.putExtra(EXTRA_MESSAGE, id);
        startActivity(intent);
    }
}