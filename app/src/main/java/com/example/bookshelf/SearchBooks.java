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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class SearchBooks extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.bookshelf.MESSAGE";
    private TextView uidTv;
    private ListView searchResults;
    private EditText searchBar;
    private Button searchButton;
    private ArrayAdapter<String> bookAdapter;
    private ArrayList<String> resultList; // using string dummy values until book objects can be used

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_books);

        final String userId = getIntent().getStringExtra("UserID");
//        uidTv = findViewById(R.id.uid_search);
//        uidTv.setText(userId);

        searchBar = findViewById(R.id.search_bar);
        searchButton = findViewById(R.id.search_button);
        searchResults = findViewById(R.id.search_results);

        resultList = new ArrayList<>();
        bookAdapter = new ArrayAdapter<>(this, R.layout.content, resultList);
        searchResults.setAdapter(bookAdapter);

        final String []books ={"A Song of Ice and Fire", "Swan Song", "Frankenstein", "Monster", "Monsters Inc."};

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                matchBook(books);
            }
        });

        searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openBookDescription(view);
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
                        Intent booksIntent = new Intent(getApplicationContext(), UserBooks.class);
                        booksIntent.putExtra("UserID", userId);
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
     * Takes an array of strings and matches it against a user string. Matches are added to the
     * custom list and displayed on screen as "results".
     *
     * @param booklist array of books to be matched against the user input.
     */
    public void matchBook(String[] booklist) {
        bookAdapter.clear();
        String check = searchBar.getText().toString();
        Pattern pattern = Pattern.compile(check, Pattern.CASE_INSENSITIVE);
        for(int i = 0; i < booklist.length; i++){
            Matcher matcher = pattern.matcher(booklist[i]);
            if(matcher.find() && !check.isEmpty()){
                bookAdapter.add(booklist[i]);
            }
        }
    }

    /**
     * Starts a new activity showing the details of the book that was clicked on.
     *
     * @param view TextView holding the info of the selected book.
     */
    public void openBookDescription(View view) {
        Intent intent = new Intent(this, BookActivity.class);
        TextView textView = (TextView) view;
        String message = textView.getText().toString();
        // we want the message to be the book ID corresponding to the selected book
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}