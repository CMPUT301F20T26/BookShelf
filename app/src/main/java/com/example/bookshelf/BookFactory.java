package com.example.bookshelf;


import com.google.firebase.firestore.CollectionReference;

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
    public void Title(String title) {
        thisBook.setTitle(title);
        bookMap.put("title", title);
    }

    /**
     * Author.
     *
     * @param author the author
     */
    public void Author(String author) {
        thisBook.setAuthor(author);
        bookMap.put("author", author);
    }

    /**
     * Isbn.
     *
     * @param ISBN the isbn
     */
    public void ISBN(Long ISBN) {
        thisBook.setISBN(ISBN);
        bookMap.put("isbn", ISBN);
    }

    /**
     * Photo url.
     *
     * @param photoURL the photo url
     */
    public void PhotoURL(String photoURL) {
        thisBook.setPhotoURL(photoURL);
        bookMap.put("photoURL", photoURL);
    }

    /**
     * Status.
     *
     * @param status the status
     */
    public void Status(Book.BookStatus status) {
        thisBook.setStatus(status);
        bookMap.put("status", status);
    }

    /**
     * Owner username.
     *
     * @param ownerUsername the owner username
     */
    public void OwnerUsername(String ownerUsername) {
        thisBook.setOwnerUsername(ownerUsername);
        bookMap.put("ownerUsername", ownerUsername);
    }

    /**
     * Description.
     *
     * @param description the description
     */
    public void Description(String description) {
        thisBook.setDescription(description);
        bookMap.put("description", description);
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
        // add id to book and bookmap
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
