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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The type Add book fragment.
 */
public class AddBookFragment extends DialogFragment {
    private EditText title;
    private EditText author;
    private EditText isbn;
    private EditText description;
    private DialogListener listener;
    String owner;


    /**
     * The interface Dialog listener.
     */
    public interface DialogListener{
        /**
         * Add book.
         *
         * @param book the book
         */
        void add_Book(Book book);

        /**
         * Edit book.
         *
         * @param book   the book
         * @param title  the title
         * @param author the author
         * @param isbn   the isbn
         * @param des    the des
         */
        void edit_Book(Book book, String title, String author, Long isbn, String des);
        }

    /**
     * New instance add book fragment.
     *
     * @param book the book
     * @return the add book fragment
     */
    static AddBookFragment newInstance(Book book){
        Bundle args = new Bundle();
        args.putSerializable("book", book);

        AddBookFragment fragment = new AddBookFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DialogListener){
            listener = (DialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Book argBook;
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_add_book, null);
        title = view.findViewById(R.id.Title_add);
        author = view.findViewById(R.id.Author_add);
        isbn = view.findViewById(R.id.ISBN_add);
        description = view.findViewById(R.id.Description_add);
        final FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("books");

        BookFactory bookFactory = new BookFactory(collectionReference);

        /**
         * If the object is to be edited then initialize add fields to Gear's existing fields
         */
        if(getArguments()!=null){
            argBook = (Book) getArguments().getSerializable("book");
            assert argBook != null;

            title.setText(argBook.getTitle());
            author.setText(argBook.getAuthor());
            isbn.setText(String.valueOf(argBook.getISBN()));
            description.setText(argBook.getDescription());
        }
        else {
            argBook = null;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Book")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        db.collection("users").document(user.getUid()).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()) {
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            owner = documentSnapshot.getData().get("username").toString();
                        String title_new = title.getText().toString();
                        String author_new = author.getText().toString();
                        String isbn_new = isbn.getText().toString();
                        String des_new = description.getText().toString();
                        if (title_new.isEmpty() || author_new.isEmpty() || isbn_new.isEmpty()) {
                            Toast toast = Toast.makeText((Objects.requireNonNull(getActivity())).getBaseContext(), "Required Fields Empty! Please try again.", Toast.LENGTH_LONG);
                            toast.show();
                            return;
                        }
                        Book addBook;
                        Long isbn_add = Long.parseLong(isbn.getText().toString());

                        //Firebase Authentication instance


                        // using the right constructor for gear
                        if(des_new.isEmpty()){
                            addBook = new Book(title_new,author_new,isbn_add, owner);
                            Map<String, Object> book = new HashMap<>();
                            book.put("author", author_new);
                            book.put("description", "");
                            book.put("isbn", isbn_new);
                            book.put("ownerUsername", owner);
                            book.put("photoUrl","");
                            book.put("status",addBook.getStatus());
                            book.put("title", title_new);
                            collectionReference
                                    .document(isbn_new)
                                    .set(book);}
                        else{
                            addBook = new Book(title_new,author_new,des_new,isbn_add, owner);
                            Map<String, Object> book = new HashMap<>();

                            book.put("author", author_new);
                            book.put("description", des_new);
                            book.put("isbn", isbn_new);
                            book.put("ownerUsername", owner);
                            book.put("photoUrl","");
                            book.put("status",addBook.getStatus());
                            book.put("title", title_new);
                            collectionReference
                                    .document(isbn_new)
                                    .set(book);}

                        // check if gear is to be edited or added
                        if (getArguments()!=null){
                            listener.edit_Book(argBook, title_new,author_new,isbn_add,des_new);
                        }
                        else{
                            listener.add_Book(addBook);
                        }
                                        }
                                    }
                                });
                    }}).create();


    }
}
