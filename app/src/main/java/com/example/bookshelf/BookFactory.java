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

    // TODO: use FireBaseHelper

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
    Book get(final DocumentSnapshot bookDoc, final String bookID){

        final Book res = new Book();
        final String isbnString;
        Long isbn = 0L;
                res.setBookID(bookID);
            if(bookDoc.get("isbn") != null) {
                isbnString = bookDoc.getData().get("isbn").toString().replace("-", "");
                isbn = Long.parseLong(isbnString);
                res.setBookID(bookID);
            }
            if(bookDoc.get("title") != null) {
                res.setTitle(bookDoc.get("title").toString());
            }
            if(bookDoc.get("ownerUsername") != null) {
                res.setOwnerUsername(bookDoc.get("ownerUsername").toString());
            }
            if(bookDoc.get("author") != null) {
                res.setAuthor(bookDoc.get("author").toString());
                res.setISBN(isbn);
            }
            if(bookDoc.get("coverImage") != null) {
                res.setPhotoURL(bookDoc.get("coverImage").toString());
            }
            if(bookDoc.get("description") != null) {
                res.setDescription(bookDoc.get("description").toString());
            }

            switch(bookDoc.get("status").toString()) {
                case "Available": res.setStatus(Book.BookStatus.Available);
            }

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
