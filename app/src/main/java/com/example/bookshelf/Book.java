package com.example.bookshelf;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;


/**
 * The type Book.
 */
public class Book implements Serializable {
    /**
     * The enum Book status.
     */
    public enum BookStatus{
        /**
         * Available book status.
         */
        Available,
        /**
         * Requested book status.
         */
        Requested,
        /**
         * Accepted book status.
         */
        Accepted,
        /**
         * Borrowed book status.
         */
        Borrowed,
        /**
         * Loaned book status.
         */
        Loaned};


    //Book Attributes
    private String title;
    private String author;
    private Long isbn;
    private String photoURL;
    private BookStatus status;
    private String ownerUsername;
    private String description;
    private String BookID;

    /**
     * Instantiates a new Book.
     * Used for creating already existing books
     * @param title         the title
     * @param author        the author
     * @param isbn          the isbn
     * @param photoURL      the photo url
     * @param status        the status
     * @param ownerUsername the owner username
     * @param description   the description
     */
    public Book(String title, String author, Long isbn, String photoURL, BookStatus status , String ownerUsername, String description) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.status = status;
        this.photoURL = photoURL;
        this.description = description;
        this.ownerUsername = ownerUsername;
    }


    /**
     * Gets book id.
     *
     * @return the book id
     */
    public String getBookID() {
        return BookID;
    }

    /**
     * Sets book id.
     *
     * @param bookID the book id
     */
    public void setBookID(String bookID) {
        BookID = bookID;
    }

    /**
     * Instantiates a new Book.
     */
    public Book() {}

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets author.
     *
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets author.
     *
     * @param author the author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets isbn.
     *
     * @return the isbn
     */
    public Long getIsbn() {
        return isbn;
    }

    /**
     * Sets isbn.
     *
     * @param isbn the isbn
     */
    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    /**
     * Gets photo url.
     *
     * @return the photo url
     */
    public String getPhotoURL() {
        return photoURL;
    }

    /**
     * Sets photo url.
     *
     * @param photoURL the photo url
     */
    public void setCoverImage(String photoURL) {
        this.photoURL = photoURL;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public BookStatus getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(BookStatus status) {
        this.status = status;
    }
    public void setStatus(String status) {
        if(status.equals("Available")){
            this.status = BookStatus.Available;
        }
        else if(status.equals("Requested")){
            this.status = BookStatus.Requested;
        }
        else if(status.equals("Accepted")){
            this.status = BookStatus.Accepted;
        }
        else if(status.equals("Borrowed")){
            this.status = BookStatus.Borrowed;
        }else if(status.equals("Loaned")){
            this.status = BookStatus.Loaned;
        }
        else{
            Log.d("Error","Invalid Status");
        }
    }

    /**
     * Gets owner username.
     *
     * @return the owner username
     */
    public String getOwnerUsername() {
        return ownerUsername;
    }

    /**
     * Sets owner username.
     *
     * @param ownerUsername the owner username
     */
    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return BookID.equals(book.BookID);
    }

    /**
     * Returns a book map that can be pushed to firebase
     * @return
     */
    public HashMap<String, Object> getBookFirebaseMap(){
        HashMap<String, Object> bookMap = new HashMap<String, Object>();
        bookMap.put("author", this.author);
        bookMap.put("coverImage", this.photoURL);
        bookMap.put("description",this.description);
        bookMap.put("isbn", this.isbn);
        bookMap.put("ownerUsername", this.ownerUsername);
        bookMap.put("status", this.status);
        bookMap.put("title", this.title);

        return bookMap;

    }

    @Override
    public int hashCode() {
        return Objects.hash(BookID);
    }
}

