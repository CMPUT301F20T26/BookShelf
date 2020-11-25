package com.example.bookshelf;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The type Add book fragment.
 */
public class AddBookFragment extends DialogFragment {

    // Layout Variables
    private EditText title;
    private EditText author;
    private EditText isbn;
    private EditText description;
    private DialogListener listener;
    private String ownerUsername;

    //Position
    private int position;

    //Current User
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //Database initialization
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Books Collection reference
    private DocumentReference userDocumentRef = db.collection("users").document(user.getUid());

    /**
     * The interface Dialog listener.
     */
    public interface DialogListener{

        void add_Book(String title, String author, Long isbn, String photoURL, String ownerUsername, String description);

        void edit_Book(String title, String author, Long isbn, String photoURL, String ownerUsername, String description, Book.BookStatus status, Integer position);
    }

    /**
     * New instance add book fragment.
     *
     * @param book the book
     * @return the add book fragment
     */
    static AddBookFragment newInstance(Book book, int pos){
        Bundle args = new Bundle();
        args.putSerializable("book", book);
        args.putInt("Position", pos);

        AddBookFragment fragment = new AddBookFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * On fragment attach to context
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DialogListener){
            listener = (DialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DialogListener");
        }
    }

    /**
     * Crating dialog fragment
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Book argBook = null;
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_add_book, null);

        title = view.findViewById(R.id.Title_add);
        author = view.findViewById(R.id.Author_add);
        isbn = view.findViewById(R.id.ISBN_add);
        description = view.findViewById(R.id.Description_add);
        Book.BookStatus status = null;

        /**
         * If the object is to be edited then initialize add fields to Gear's existing fields
         */
        if(getArguments()!=null){
            argBook = (Book) getArguments().getSerializable("book");
            position = getArguments().getInt("Position");
            assert argBook != null;

            title.setText(argBook.getTitle());
            author.setText(argBook.getAuthor());
            isbn.setText(String.valueOf(argBook.getIsbn()));
            description.setText(argBook.getDescription());
            status = argBook.getStatus();
        }


        // TODO: integrate with BookFactory
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final Book finalArgBook = argBook;
        final Book.BookStatus finalStatus = status;
        return builder
                .setView(view)
                .setTitle("Book")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        userDocumentRef.get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot documentSnapshot = task.getResult();

                                            ownerUsername = documentSnapshot.getData().get("username").toString();
                                            String title_new = title.getText().toString();
                                            String author_new = author.getText().toString();
                                            String isbn_new = isbn.getText().toString();
                                            String des_new = description.getText().toString();

                                            if (title_new.isEmpty() || author_new.isEmpty() || isbn_new.isEmpty()) {
                                                Toast toast = Toast.makeText((Objects.requireNonNull(getActivity())).getBaseContext(), "Required Fields Empty! Please try again.", Toast.LENGTH_LONG);
                                                toast.show();
                                                return;
                                            }

                                            Long isbn_long = Long.parseLong(isbn.getText().toString());
                                            String photoURL = isbn_new + ".png";

                                            // check if gear is to be edited or added
                                            if (finalArgBook != null) {
                                                listener.edit_Book(title_new, author_new, isbn_long, photoURL, ownerUsername, des_new, finalStatus, position);
                                            } else {
                                                listener.add_Book(title_new, author_new, isbn_long, photoURL, ownerUsername, des_new);
                                            }
                                        }
                                    }
                                });
                    }
                }).create();
    }
};