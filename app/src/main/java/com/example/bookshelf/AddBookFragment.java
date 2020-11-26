package com.example.bookshelf;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;


/**
 * The type Add book fragment.
 */
public class AddBookFragment extends DialogFragment {
    // Layout Variables
    private EditText titleEt;
    private EditText authorEt;
    private EditText isbnEt;
    private EditText descriptionEt;
    private ImageView bookIm;
    private Button pictureBtn;

    //
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

    //Firebase Storage instance
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference;
    private Uri imageUri;

    private String photoURL = null;

    /**
     * The interface Dialog listener.
     */
    public interface DialogListener{

        void add_Book(String title, String author, Long isbn, String photoURL, String ownerUsername, String description);

        void edit_Book(String title, String author, Long isbn, String photoURL, String ownerUsername, String description, Book.BookStatus status, Integer position, String bookId);
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
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_book_fragment, null);

        titleEt = view.findViewById(R.id.Title_add);
        authorEt = view.findViewById(R.id.Author_add);
        isbnEt = view.findViewById(R.id.ISBN_add);
        descriptionEt = view.findViewById(R.id.Description_add);
        bookIm = view.findViewById(R.id.cover_image);
        pictureBtn = view.findViewById(R.id.picture_button);

        //Instantiating the storage reference
        storageReference = storage.getReference();

        Book.BookStatus status = null;

        /**
         * If the object is to be edited then initialize add fields to Gear's existing fields
         */
        if(getArguments()!=null){
            argBook = (Book) getArguments().getSerializable("book");
            position = getArguments().getInt("Position");
            assert argBook != null;

            titleEt.setText(argBook.getTitle());
            authorEt.setText(argBook.getAuthor());
            isbnEt.setText(String.valueOf(argBook.getIsbn()));
            descriptionEt.setText(argBook.getDescription());
            pictureBtn.setText("Change Image");
            status = argBook.getStatus();

            //Setting Book image.
            //Getting book cover image
            photoURL = "Book Images/" + argBook.getPhotoURL();
            storageReference.child(photoURL).getDownloadUrl().addOnSuccessListener(
                    new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(bookIm);
                        }
                    }
            ).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Failed to fetch image.", Toast.LENGTH_SHORT).show();
                }
            });
        }


        //ImageView on click listener to chang profile picture
        pictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePictureFromDevice();
            }
        });


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
                                            String title_new = titleEt.getText().toString();
                                            String author_new = authorEt.getText().toString();
                                            String isbn_new = isbnEt.getText().toString();
                                            String des_new = descriptionEt.getText().toString();

                                            if (title_new.isEmpty() || author_new.isEmpty() || isbn_new.isEmpty()) {
                                                Toast toast = Toast.makeText((Objects.requireNonNull(getActivity())).getBaseContext(), "Required Fields Empty! Please try again.", Toast.LENGTH_LONG);
                                                toast.show();
                                                return;
                                            }

                                            Long isbn_long = Long.parseLong(isbnEt.getText().toString());
                                            String coverUrl = isbn_new + ".png";

                                            // check if gear is to be edited or added
                                            if (finalArgBook != null) {
                                                listener.edit_Book(title_new, author_new, isbn_long, coverUrl, ownerUsername, des_new, finalStatus, position, finalArgBook.getBookID());
                                            } else {
                                                listener.add_Book(title_new, author_new, isbn_long, coverUrl, ownerUsername, des_new);
                                            }
                                        }
                                    }
                                });
                    }
                }).create();
    }

    private void choosePictureFromDevice() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            bookIm.setImageURI(imageUri);
            uploadPictureToDatabase();
        }
    }

    private void uploadPictureToDatabase() {
        //Loading bar
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Uploading Image...");
        pd.show();

        //Get the entered isbn on a new book
        //TODO: This is really Inefficient and could be null if isbn field is null.
        if(photoURL==null){
            photoURL = "Book Images/" + isbnEt.getText().toString() + ".png";
        }

        //Uploading Book
        StorageReference newImgeRef = storageReference.child(photoURL);
        newImgeRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Toast.makeText(getContext(), "Image updated successfully", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getContext(), "Image failed to successfully upload.", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progessPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Percentage: " + (int) progessPercent + "%");
            }
        });
    }
};