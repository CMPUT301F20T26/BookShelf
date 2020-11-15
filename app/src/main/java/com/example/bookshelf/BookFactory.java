package com.example.bookshelf;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * BookFactory - For creating new books.
 */
public class BookFactory {

    /**
     * Title.
     *
     * @param title the title
     */
    public BookFactory Title(String title) {
        thisBook.setTitle(title);
        bookMap.put("title", title);
        return this;
    }

    /**
     * Author.
     *
     * @param author the author
     */
    public BookFactory Author(String author) {
        thisBook.setAuthor(author);
        bookMap.put("author", author);
        return this;
    }

    /**
     * Isbn.
     *
     * @param ISBN the isbn
     */
    public BookFactory ISBN(Long ISBN) {
        thisBook.setISBN(ISBN);
        bookMap.put("isbn", ISBN);
        return this;
    }

    /**
     * Photo url.
     *
     * @param photoURL the photo url
     */
    public BookFactory PhotoURL(String photoURL) {
        thisBook.setPhotoURL(photoURL);
        bookMap.put("photoURL", photoURL);
        return this;
    }

    /**
     * Status.
     *
     * @param status the status
     */
    public BookFactory Status(Book.BookStatus status) {
        thisBook.setStatus(status);
        bookMap.put("status", status);
        return this;
    }

    /**
     * Owner username.
     *
     * @param ownerUsername the owner username
     */
    public BookFactory OwnerUsername(String ownerUsername) {
        thisBook.setOwnerUsername(ownerUsername);
        bookMap.put("ownerUsername", ownerUsername);
        return this;
    }

    /**
     * Description.
     *
     * @param description the description
     */
    public BookFactory Description(String description) {
        thisBook.setDescription(description);
        bookMap.put("description", description);
        return this;
    }

    private Book thisBook;
    /**
     * The Book map.
     */
    Map<String, Object> bookMap;
    CollectionReference bookCollectionReference;

    /**
     * Instantiates a new Book factory.
     *
     * @param bookReference the firebase book collection reference
     */
    BookFactory(CollectionReference bookReference)
    {
        thisBook = new Book();
        bookMap = new HashMap<>();
        bookCollectionReference = bookReference;
    }

    /**
     * Resets the fields of the current factory build, allowing for a new book to be built.
     */
    void New()
    {
        thisBook = new Book();
        bookMap = new HashMap<>();
    }


    /**
     * Gets a book from Firebase, given the book ID.
     *
     * @param bookID the book id
     * @return the book
     */
    Book get(final String bookID){
        final Book res = new Book();
        bookCollectionReference.document(bookID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot bookDoc = task.getResult();
                            res.setBookID(bookID);
                            res.setTitle(bookDoc.get("title").toString());
                            res.setOwnerUsername(bookDoc.get("owner").toString());
                            res.setTitle(bookDoc.get("title").toString());
                            // TODO : rest of fields here
                        }
                        else {
                            // TODO: throw exception
                        }
                    }
                });
        return res;
    }

    /**
     * Builds the book, which determines the book ID.
     * This method should be used when pushing new books to firebase.
     *
     * @return the book
     */
    Book build(){
        // get time of book creation
        // this is used to calculate the unique book ID
        long now = new Date().getTime();
        String id = String.format("%x", Objects.hash(now, thisBook.getTitle()));
        // add id to book and bookMap
        thisBook.setBookID(id);
        bookMap.put("bookID", id);
        // push to firebase
        bookCollectionReference
                .document(id)
                .set(thisBook);
        // return built book
        return thisBook;
    }


    /*
    static Book scanToCreate() {
        // to implement later...
        return null;
    }

    static Book buildNow(){

        return null;
    }
     */


}
