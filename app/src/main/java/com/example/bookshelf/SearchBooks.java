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

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchBooks extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.bookshelf.MESSAGE";
    private TextView uidTv;
    private ListView searchResults;
    private EditText searchBar;
    private Button searchButton;
    private ArrayAdapter<String> bookAdapter;
    private ArrayList<String> resultList; // using string dummy values until book objects can be used
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_books);

        final String userId = getIntent().getStringExtra("UserID");
//        uidTv = findViewById(R.id.uid_search);
//        uidTv.setText(userId);
        db = FirebaseFirestore.getInstance();

        searchBar = findViewById(R.id.search_bar);
        searchButton = findViewById(R.id.search_button);
        searchResults = findViewById(R.id.search_results);

        resultList = new ArrayList<>();
        bookAdapter = new ArrayAdapter<>(this, R.layout.content, resultList);
        searchResults.setAdapter(bookAdapter);

        final HashMap<String, String> idName = new HashMap<String, String>(); //dumb naming convention, but its a hashmap of id and book name pairs
        final ArrayList<String> idList = new ArrayList<String>();

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                idName.clear();
                bookAdapter.clear();
                db.collection("books")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        idName.put(document.getId(), document.getData().get("title").toString());
                                        matchBook(idName, idList, document.getId());
                                    }
                                }
                            }
                        });
            }
        });

        searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openBookDescription(idList.get(position));
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
     * Takes a string and matches it against a user string. Matches are added to the
     * custom list and displayed on screen as "results".
     *
     * @param pair id-name pairs
     * @param idList list of book ids
     * @param id id to be matched
     */
    public void matchBook(HashMap<String, String> pair, ArrayList<String> idList, String id) {
        String book = pair.get(id);
        String check = searchBar.getText().toString();
        Pattern pattern = Pattern.compile(check, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(book);
            if(matcher.find() && !check.isEmpty()){
                bookAdapter.add(book);
                idList.add(id);
            } else {
                pair.remove(id);
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