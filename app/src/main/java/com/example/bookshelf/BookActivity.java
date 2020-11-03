package com.example.bookshelf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class BookActivity extends AppCompatActivity implements MakeRequestFragment.OnFragmentInteractionListener {
    private ImageView displayPic;
    private TextView title;
    private TextView description;
    private TextView author;
    private TextView ISBN;
    private TextView owner;
    private TextView status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        //intent should provide bookID, use to access object and set fields
        //right now it only contains a string of the book's title

        Intent intent = getIntent();
        String message = intent.getStringExtra(SearchBooks.EXTRA_MESSAGE);

        displayPic = findViewById(R.id.imageView);
        title = findViewById(R.id.title_text);
        description = findViewById(R.id.book_description);
        author = findViewById(R.id.author_text);
        ISBN = findViewById(R.id.isbn_text);
        owner = findViewById(R.id.owner_text);
        status = findViewById(R.id.status_text);

        title.setText(message);



        final Button request = findViewById(R.id.request_button);

        // set onClick to create a fragment (yes/no)
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MakeRequestFragment().show(getSupportFragmentManager(), "CONFIRM");
            }
        });
        // create intent to move to user profile

    }

    public void onOkPressed(){
        //do nothing, for now
    }
}