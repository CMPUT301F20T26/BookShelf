package com.example.bookshelf;



import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * BookFactory - For creating new books.
 */
public class BookFactory {
    private final FirebaseFirestore db;
    final ArrayList<Book> temp = new ArrayList<Book>();
    private BookFactory.bookListener listener;
    public interface bookListener{
        /**
         * Add book.
         */
        void getBook(Book book, String status, Boolean own);
    }


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
        thisBook.setIsbn(ISBN);
        bookMap.put("isbn", ISBN);
        return this;
    }

    /**
     * Photo url.
     *
     * @param coverImage the photo url
     */

    public BookFactory CoverImage(String coverImage) {
        thisBook.setCoverImage(coverImage);
        bookMap.put("coverImage", coverImage);
        return this;
    }

    /**
     * Status.
     *
     * @param status the status
     */
    public BookFactory Status(Book.BookStatus status) {
        thisBook.setStatus(status.toString());
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
     * @param collectionPath the firebase book collection reference
     */
    BookFactory(String collectionPath)
    {
        thisBook = new Book();
        bookMap = new HashMap<>();
        db = FirebaseFirestore.getInstance();
        bookCollectionReference=db.collection(collectionPath);
        bookMap.put("description", "");
        bookMap.put("coverImage", "");

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
     * @return the book
     */
    Book get(final DocumentSnapshot bookDoc){

        final Book res = new Book();
        final String isbnString;
        Long isbn = 0L;

            res.setBookID(bookDoc.getId());
            if(bookDoc.getData().get("isbn") != null) {
                isbnString = bookDoc.getData().get("isbn").toString().replace("-", "");
                isbn = Long.parseLong(isbnString);
                res.setBookID(bookDoc.getId());
            }
            if(bookDoc.getData().get("title") != null) {
                res.setTitle(bookDoc.get("title").toString());
            }
            if(bookDoc.getData().get("ownerUsername") != null) {
                res.setOwnerUsername(bookDoc.get("ownerUsername").toString());
            }
            if(bookDoc.getData().get("author") != null) {
                res.setAuthor(bookDoc.get("author").toString());
                res.setIsbn(isbn);
            }
            if(bookDoc.getData().get("coverImage") != null) {
                res.setCoverImage(bookDoc.get("coverImage").toString());
            }
            if(bookDoc.getData().get("description") != null) {
                res.setDescription(bookDoc.get("description").toString());
            }
            if(bookDoc.get("status") != null){
            res.setStatus(bookDoc.get("status").toString());}

            if(bookDoc.getData().get("status") != null) {
                res.setStatus(Book.BookStatus.valueOf(bookDoc.get("status").toString()));
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
        FirebaseHelper helper = new FirebaseHelper();
        // get time of book creation
        // this is used to calculate the unique book ID
        String id = helper.add("books", bookMap);
        // add id to book and bookMap
        thisBook.setBookID(id);
        // return built book
        return thisBook;
    }
    Book edit(String id){
        bookCollectionReference
                .document(id)
                .update(bookMap);
        thisBook.setBookID(id);
        return thisBook;
    }
    void delete(String id){
        bookCollectionReference
                .document(id)
                .delete();
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
